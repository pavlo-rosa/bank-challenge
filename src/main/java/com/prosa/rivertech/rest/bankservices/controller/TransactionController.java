package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.BeneficiaryService;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import com.prosa.rivertech.rest.bankservices.utils.FilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {
    private final AccountService accountService;
    private final BeneficiaryService beneficiaryService;
    private final TransactionService transactionService;
    private final FilterResponse filterResponse;

    @Autowired
    public TransactionController(AccountService accountService, BeneficiaryService beneficiaryService, TransactionService transactionService, FilterResponse filterResponse) {
        this.accountService = accountService;
        this.beneficiaryService = beneficiaryService;
        this.filterResponse = filterResponse;
        this.transactionService = transactionService;
    }

    @GetMapping("/beneficiaries/{beneficiaryId}/accounts/{accountId}/transactions")
    public MappingJacksonValue retrieveAllTransactionsByAccount(@PathVariable Long beneficiaryId, @PathVariable Long accountId) {
        List<Transaction> accountTransactions =  transactionService.findAllByAccountId(accountId);
        return filterResponse.getMappingJacksonValue(accountTransactions, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

}
