package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    public List<Transaction> findAllByAccountId(Long accountId);

    public Transaction addDeposit(Long accountId, BigDecimal amount, String authorization);

    public Transaction addWithdrawal(Long accountId, BigDecimal amount, String authorization);

    public Transference addTransference(Long sourceAccountId, Long destinationAccountId, BigDecimal amount, String authorization);

}
