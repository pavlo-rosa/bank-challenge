package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.TransactionDepositOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransactionWithdrawalOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import com.prosa.rivertech.rest.bankservices.utils.FilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final FilterResponse filterResponse;

    @Autowired
    public TransactionController(TransactionService transactionService, FilterResponse filterResponse) {
        this.filterResponse = filterResponse;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public MappingJacksonValue retrieveAllTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> accountTransactions = transactionService.findAllByAccountId(accountId);
        return filterResponse.getMappingJacksonValue(accountTransactions, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    @PostMapping("/accounts/{accountId}/transactions/deposits")
    public MappingJacksonValue createDeposit(@PathVariable Long accountId,
                                             @Valid @RequestBody TransactionDepositOperationRequest body,
                                             @RequestHeader("Authorization") String authorization) {
        Transaction newTransaction = transactionService.addDeposit(accountId, body.getAmount(), authorization);
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);
    }

    @PostMapping("/accounts/{accountId}/transactions/withdrawals")
    public MappingJacksonValue createWithdrawal(@PathVariable Long accountId,
                                                @Valid @RequestBody TransactionWithdrawalOperationRequest body,
                                                @RequestHeader("Authorization") String authorization) {
        Transaction newTransaction = transactionService.addWithdrawal(accountId, body.getAmount(), authorization);
        return filterResponse.getMappingJacksonValue(newTransaction, filterResponse.TransactionFilter, filterResponse.TransactionFilterMapping);

    }

    @PostMapping("/accounts/{accountId}/transactions/transferences")
    public MappingJacksonValue createTransference(@PathVariable Long accountId,
                                                  @Valid @RequestBody TransferenceOperationRequest body,
                                                  @RequestHeader("Authorization") String authorization) {
        Transference newTransference = transactionService.addTransference(accountId, body.getDestination(), body.getAmount(), authorization);
        return filterResponse.getMappingJacksonValue(newTransference, filterResponse.TransferenceFilter, filterResponse.TransferenceFilterMapping);
    }

}
