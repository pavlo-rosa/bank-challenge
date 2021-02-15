package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.TransactionBasicOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import com.prosa.rivertech.rest.bankservices.utils.FilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final FilterResponse filterResponse;

    @Autowired
    public TransactionController(AccountService accountService, UserService userService, TransactionService transactionService, FilterResponse filterResponse) {
        this.accountService = accountService;
        this.userService = userService;
        this.filterResponse = filterResponse;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public MappingJacksonValue retrieveAllTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> accountTransactions = transactionService.findAllByAccountId(accountId);
        return filterResponse.getMappingJacksonValue(accountTransactions, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    //TODO: AMOUNT ALWAYS POSITIVE
    @PostMapping("/accounts/{accountId}/transactions/deposits")
    public MappingJacksonValue createDeposit(@PathVariable Long accountId, @RequestBody TransactionBasicOperationRequest body) {
        Transaction newTransaction = transactionService.addDeposit(accountId, body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    //TODO: AMOUNT ALWAYS NEGATIVE
    @PostMapping("/accounts/{accountId}/transactions/withdrawals")
    public MappingJacksonValue createWithdrawal(@PathVariable Long accountId, @RequestBody TransactionBasicOperationRequest body) {
        Transaction newTransaction = transactionService.addWithdrawal(accountId, body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);

    }

    //TODO: AMOUNT ALWAYS POSITIVE
    @PostMapping("/accounts/{accountId}/transactions/transferences")
    public MappingJacksonValue createTransference(@PathVariable Long accountId, @RequestBody TransferenceOperationRequest body) {
        Transference newTransference = transactionService.addTransference(accountId, body.getDestination(), body.getAmount());
        return filterResponse.getMappingJacksonValue(newTransference, filterResponse.TransferenceFilter, filterResponse.TransferenceFilterMapping);
    }

}
