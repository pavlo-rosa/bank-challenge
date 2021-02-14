package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.TransactionBasicOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
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
        List<Transaction> accountTransactions = transactionService.findAllByAccountId(accountId);
        return filterResponse.getMappingJacksonValue(accountTransactions, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    //TODO: AMOUNT ALWAYS POSITIVE
    @PostMapping("/beneficiaries/{beneficiaryId}/accounts/{accountId}/transactions/deposits")
    public MappingJacksonValue createDeposit(@PathVariable Long beneficiaryId, @PathVariable Long accountId, @RequestBody TransactionBasicOperationRequest body) {
        Transaction newTransaction = transactionService.addDeposit(beneficiaryId, accountId, body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    //TODO: AMOUNT ALWAYS NEGATIVE
    @PostMapping("/beneficiaries/{beneficiaryId}/accounts/{accountId}/transactions/withdrawals")
    public MappingJacksonValue createWithdrawal(@PathVariable Long beneficiaryId, @PathVariable Long accountId, @RequestBody TransactionBasicOperationRequest body) {
        Transaction newTransaction = transactionService.addWithdrawal(beneficiaryId, accountId, body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);

    }

    //TODO: AMOUNT ALWAYS POSITIVE
    @PostMapping("/beneficiaries/{beneficiaryId}/accounts/{accountId}/transactions/transferences")
    public MappingJacksonValue createTransference(@PathVariable Long beneficiaryId,  @PathVariable Long accountId, @RequestBody TransferenceOperationRequest body) {
        Transference newTransference = transactionService.addTransference(beneficiaryId, accountId, body.getDestination(), body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransference, filterResponse.TransferenceFilter, filterResponse.TransferenceFilterMapping);
    }

}
