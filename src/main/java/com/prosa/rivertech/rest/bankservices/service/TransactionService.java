package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    public List<Transaction> findAllByAccountId(Long accountId);

    public Transaction addDeposit(Long userId, Long accountId, BigDecimal amount);

    public Transaction addWithdrawal(Long userId, Long accountId, BigDecimal amount);

    public Transference addTransference(Long userId, Long sourceAccountId, Long destinationAccountId, BigDecimal amount);

}
