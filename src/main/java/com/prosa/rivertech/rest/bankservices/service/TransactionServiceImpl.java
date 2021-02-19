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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return account.getTransactions()
                .stream()
                .sorted((o1, o2) -> o2.getId().compareTo(o1.getId())
                ).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Transaction addDeposit(Long accountId, BigDecimal amount) {
        Account account = accountService.findById(accountId);
        Operation operation = operationService.findById(EnumOperationType.DEPOSIT.getId());
        return createTransaction(account, amount, operation, null);
    }

    @Transactional
    @Override
    public Transaction addWithdrawal(Long accountId, BigDecimal amount, String authorization) {
        Account account = accountService.findById(accountId);
        boolean authorized = authorizationManager.validateUser(authorization, account);
        if (!authorized) {
            throw new UnauthorizedException("Account number or Password invalid");
        }
        BigDecimal newPossibleBalance = amount.add(account.getBalance());
        if (newPossibleBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new UnauthorizedException("Insufficient balance");
        }

        Operation operation = operationService.findById(EnumOperationType.WITHDRAW.getId());
        return createTransaction(account, amount, operation, null);
    }

    @Transactional
    @Override
    public Transaction addTransference(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, String authorization) {
        Account sourceAccount = accountService.findById(sourceAccountId);
        boolean authorized = authorizationManager.validateUser(authorization, sourceAccount);
        if (!authorized) {
            throw new UnauthorizedException("Account number or Password invalid");
        }
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new UnauthorizedException("Insufficient balance");
        }
        Transaction receiptTransactionOrigin = null;
        Account destinationAccount = accountService.findById(destinationAccountId);
        Operation operation = operationService.findById(EnumOperationType.TRANSFER.getId());
        Transference newTransference = new Transference(sourceAccount, destinationAccount, amount, operation);

        newTransference = transferenceRepository.save(newTransference);
        receiptTransactionOrigin = createTransaction(sourceAccount, amount.negate(), operation, newTransference);
        createTransaction(destinationAccount, amount, operation, newTransference);

        //We return a kind or receive from sourceAccount to show the success of the operation
        return receiptTransactionOrigin;
    }

    private Transaction createTransaction(Account account, BigDecimal amount, Operation operation, Transference transference) {
        BigDecimal newBalance = account.getBalance().add(amount);
        Transaction newTransaction = new Transaction(operation, account, amount, newBalance, transference);
        newTransaction.getDestinationAccount().setBalance(newBalance);
        transactionRepository.save(newTransaction);
        return newTransaction;
    }
}
