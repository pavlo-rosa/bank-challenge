package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.config.SwaggerConfig;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDepositOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDto;
import com.prosa.rivertech.rest.bankservices.dto.TransactionWithdrawalOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.mapper.TransactionMapper;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = { SwaggerConfig.TAG_TRANSACTION })
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
    @ApiOperation(value = "Retrieve all transactions by accountId")
    public ResponseEntity<List<TransactionDto>> retrieveAllTransactionsByAccount(@PathVariable Long accountId) {
        List<Transaction> accountTransactions = transactionService.findAllByAccountId(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountTransactions.stream()
                        .map(transactionMapper::mapToDto)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/accounts/{accountId}/transactions/deposits")
    @ApiOperation(value = "Make a deposit to account")
    public ResponseEntity<TransactionDto> createDeposit(@PathVariable Long accountId,
                                             @Valid @RequestBody TransactionDepositOperationRequest body) {
        Transaction newTransaction = transactionService.addDeposit(accountId, body.getAmount());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(newTransaction));
    }

    @PostMapping("/accounts/{accountId}/transactions/withdrawals")
    @ApiOperation(value = "Make a withdrawal from account")
    public ResponseEntity<TransactionDto> createWithdrawal(@PathVariable Long accountId,
                                                @Valid @RequestBody TransactionWithdrawalOperationRequest body,
                                                @RequestHeader("Authorization") String authorization) {
        Transaction newTransaction = transactionService.addWithdrawal(accountId, body.getAmount(), authorization);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(newTransaction));
    }

    @PostMapping("/accounts/{accountId}/transactions/transferences")
    @ApiOperation(value = "Make a transference from one account to another")
    public ResponseEntity<TransactionDto> createTransference(@PathVariable Long accountId,
                                                  @Valid @RequestBody TransferenceOperationRequest body,
                                                  @RequestHeader("Authorization") String authorization) {
        Transaction receiptTransactionOrigin = transactionService.addTransference(accountId, body.getDestination(), body.getAmount(), authorization);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.mapToDto(receiptTransactionOrigin));
    }

}
