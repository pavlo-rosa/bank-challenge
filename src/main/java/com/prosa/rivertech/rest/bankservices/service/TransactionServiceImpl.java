package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.dto.EnumOperationType;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
import com.prosa.rivertech.rest.bankservices.exception.UnauthorizedException;
import com.prosa.rivertech.rest.bankservices.repository.TransactionRepository;
import com.prosa.rivertech.rest.bankservices.repository.TransferenceRepository;
import com.prosa.rivertech.rest.bankservices.utils.AuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final OperationService operationService;
    private final TransferenceRepository transferenceRepository;
    private final AuthorizationManager authorizationManager;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService, OperationService operationService, TransferenceRepository transferenceRepository, AuthorizationManager authorizationManager) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.operationService = operationService;
        this.transferenceRepository = transferenceRepository;
        this.authorizationManager = authorizationManager;
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        Account account = accountService.findById(accountId);
        return account.getTransactions();
    }

    @Transactional
    @Override
    public Transaction addDeposit(Long accountId, BigDecimal amount, String authorization) {
        Operation operation = operationService.findById(EnumOperationType.DEPOSIT.getId());
        Account account = accountService.findById(accountId);
        boolean authorized = authorizationManager.validateUser(authorization, account);
        if (!authorized) {
            throw new UnauthorizedException("Unauthorized operation");
        }
        return createTransaction(account, amount, operation, null);
    }

    @Transactional
    @Override
    public Transaction addWithdrawal(Long accountId, BigDecimal amount, String authorization) {
        Operation operation = operationService.findById(EnumOperationType.WITHDRAW.getId());
        Account account = accountService.findById(accountId);
        boolean authorized = authorizationManager.validateUser(authorization, account);
        if (!authorized) {
            throw new UnauthorizedException("Account number or Password invalid.");
        }
        return createTransaction(account, amount, operation, null);
    }

    @Transactional
    @Override
    public Transference addTransference(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, String authorization) {
        Operation operation = operationService.findById(EnumOperationType.TRANSFER.getId());
        Account sourceAccount = accountService.findById(sourceAccountId);
        boolean authorized = authorizationManager.validateUser(authorization, sourceAccount);
        if (!authorized) {
            throw new UnauthorizedException("Unauthorized operation");
        }
        Account destinationAccount = accountService.findById(destinationAccountId);
        Transference newTransference = new Transference(sourceAccount, destinationAccount, amount, operation);
        newTransference = transferenceRepository.save(newTransference);
        createTransaction(sourceAccount, amount.negate(), operation, newTransference);
        createTransaction(destinationAccount, amount, operation, newTransference);
        return newTransference;
    }

    private Transaction createTransaction(Account account, BigDecimal amount, Operation operation, Transference transference) {

        BigDecimal newBalance = account.getBalance().add(amount);
        Transaction newTransaction = new Transaction(operation, account, amount, newBalance, transference);
        //TODO: Verify
//        List<Transaction> accountTransactions = account.getTransactions();
//        accountTransactions.add(newTransaction);
//        account.setTransactions(accountTransactions);
//        account.setBalance(newBalance);
//        accountService.save(account);
        newTransaction.getDestinationAccount().setBalance(newBalance);
        transactionRepository.save(newTransaction);
        return newTransaction;
    }
}
