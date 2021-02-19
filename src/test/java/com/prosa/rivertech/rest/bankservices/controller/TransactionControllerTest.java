package com.prosa.rivertech.rest.bankservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDepositOperationRequest;
import com.prosa.rivertech.rest.bankservices.dto.TransactionDto;
import com.prosa.rivertech.rest.bankservices.dto.TransferenceOperationRequest;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.mapper.AccountMapper;
import com.prosa.rivertech.rest.bankservices.mapper.TransactionMapper;
import com.prosa.rivertech.rest.bankservices.service.TransactionService;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    @MockBean
    private TransactionMapper transactionMapper;

    @Test
    void createDeposit_Basic() throws Exception {
        TransactionDepositOperationRequest body = new TransactionDepositOperationRequest();
        body.setAmount(BigDecimal.valueOf(45));

        Operation operation = mock(Operation.class);
        Account account = mock(Account.class);

        Transaction transactionResult = new Transaction(operation, account, body.getAmount(), BigDecimal.valueOf(200).add(body.getAmount()), null);
        transactionResult.setId(456L);
        TransactionDto transactionResultDto = new TransactionDto(transactionResult.getId(), "deposit", transactionResult.getAmount(), transactionResult.getBalance());

        when(transactionService.addDeposit(1L, body.getAmount())).thenReturn(transactionResult);
        when(transactionMapper.mapToDto(transactionResult)).thenReturn(transactionResultDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(body);
        String jsonResponse = mapper.writeValueAsString(transactionResultDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts/{accountId}/transactions/deposits", 1)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void createWithdrawal_Basic() throws Exception {
        TransactionDepositOperationRequest body = new TransactionDepositOperationRequest();
        body.setAmount(BigDecimal.valueOf(-45));
        String authorization = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Operation operation = mock(Operation.class);
        Account account = mock(Account.class);

        Transaction transactionResult = new Transaction(operation, account, body.getAmount(), BigDecimal.valueOf(200).add(body.getAmount()), null);
        transactionResult.setId(456L);
        TransactionDto transactionResultDto = new TransactionDto(transactionResult.getId(), "withdrawal", transactionResult.getAmount(), transactionResult.getBalance());

        when(transactionService.addWithdrawal(1L, body.getAmount(), authorization) ).thenReturn(transactionResult);
        when(transactionMapper.mapToDto(transactionResult)).thenReturn(transactionResultDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(body);
        String jsonResponse = mapper.writeValueAsString(transactionResultDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts/{accountId}/transactions/withdrawals", 1)
                .content(jsonRequest)
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void retrieveAllTransactionsByAccount_Basic() throws Exception {

        when(transactionService.findAllByAccountId(1L)).thenReturn(new ArrayList<Transaction>());

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(new ArrayList<TransactionDto>());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/{accountId}/transactions/", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }


    @Test
    void createTransference_Basic() throws Exception {
        TransferenceOperationRequest body = new TransferenceOperationRequest();
        body.setAmount(BigDecimal.valueOf(45));
        body.setDestination(3L);
        String authorization = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Operation operation = mock(Operation.class);
        Account account = mock(Account.class);

        Transaction transactionResult = new Transaction(operation, account, body.getAmount(), BigDecimal.valueOf(200).add(body.getAmount().negate()), null);
        transactionResult.setId(456L);
        TransactionDto transactionResultDto = new TransactionDto(transactionResult.getId(), "withdrawal", transactionResult.getAmount(), transactionResult.getBalance());

        when(transactionService.addTransference(1L, body.getDestination(), body.getAmount(), authorization) ).thenReturn(transactionResult);
        when(transactionMapper.mapToDto(transactionResult)).thenReturn(transactionResultDto);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(body);
        String jsonResponse = mapper.writeValueAsString(transactionResultDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts/{accountId}/transactions/transferences", 1)
                .content(jsonRequest)
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(jsonResponse));
    }
}