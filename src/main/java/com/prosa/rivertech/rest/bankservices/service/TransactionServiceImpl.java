package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        Optional<Account> accountOptional = Optional.ofNullable(accountService.findById(accountId));
        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found id: " + accountId);
        }
        Account account = accountOptional.get();
        System.out.println(account.getTransactions());
        return account.getTransactions();
    }


}
