package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    public List<Transaction> findAllByAccountId(Long accountId);

    public Transaction addDeposit(Long beneficiaryId, Long accountId, BigDecimal amount);

    public Transaction addWithdrawal(Long beneficiaryId, Long accountId, BigDecimal amount);

    public Transference addTransference(Long beneficiaryId, Long sourceAccountId, Long destinationAccountId, BigDecimal amount);

}
