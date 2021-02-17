package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.TransactionDepositOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDto;
import com.prosa.rivertech.rest.bankservices.dto.TransactionWithdrawalOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.mapper.TransactionMapper;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> retrieveAllTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> accountTransactions = transactionService.findAllByAccountId(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountTransactions.stream()
                        .map(transactionMapper::mapToDto)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/accounts/{accountId}/transactions/deposits")
    public ResponseEntity<TransactionDto> createDeposit(@PathVariable Long accountId,
                                             @Valid @RequestBody TransactionDepositOperationRequest body,
                                             @RequestHeader("Authorization") String authorization) {
        Transaction newTransaction = transactionService.addDeposit(accountId, body.getAmount(), authorization);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(newTransaction));
    }

    @PostMapping("/accounts/{accountId}/transactions/withdrawals")
    public ResponseEntity<TransactionDto> createWithdrawal(@PathVariable Long accountId,
                                                @Valid @RequestBody TransactionWithdrawalOperationRequest body,
                                                @RequestHeader("Authorization") String authorization) {
        Transaction newTransaction = transactionService.addWithdrawal(accountId, body.getAmount(), authorization);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(newTransaction));
    }

    @PostMapping("/accounts/{accountId}/transactions/transferences")
    public ResponseEntity<TransactionDto> createTransference(@PathVariable Long accountId,
                                                  @Valid @RequestBody TransferenceOperationRequest body,
                                                  @RequestHeader("Authorization") String authorization) {
        Transaction receiptTransactionOrigin = transactionService.addTransference(accountId, body.getDestination(), body.getAmount(), authorization);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(receiptTransactionOrigin));
    }

}
