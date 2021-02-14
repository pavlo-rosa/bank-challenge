package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;

import java.util.List;

public interface TransactionService {

    public List<Transaction> findAllByAccountId(Long accountId);


}
