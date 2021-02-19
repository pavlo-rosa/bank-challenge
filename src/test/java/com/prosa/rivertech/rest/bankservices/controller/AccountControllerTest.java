package com.prosa.rivertech.rest.bankservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosa.rivertech.rest.bankservices.dto.AccountCreateRequest;
import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.dto.UserDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.mapper.AccountMapper;
import com.prosa.rivertech.rest.bankservices.mapper.UserMapper;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private AccountMapper accountMapper;
    @MockBean
    private AccountService accountService;


    @Test
    void retrieveAllAccounts_Basic() throws Exception {

        User user = mock(User.class);
        UserDto userDto = new UserDto(1L, "Lisa");//mock(UserDto.class);

        Account account1 = new Account(user, "1234", "6721613755520555");
        account1.setId(44L);
        Account account2 = new Account(user, "1234", "6721613755520939");
        account2.setId(45L);

        List<Account> accounts = Arrays.asList(account1, account2);

        AccountDto accountDto1 = new AccountDto(44L, "6721613755520555", userDto, BigDecimal.ZERO);
        AccountDto accountDto2 = new AccountDto(45L, "6721613755520939", userDto, BigDecimal.ZERO);

        when(accountService.findAll()).thenReturn(accounts);
        when(accountMapper.mapToDto(accounts.get(0))).thenReturn(accountDto1);
        when(accountMapper.mapToDto(accounts.get(1))).thenReturn(accountDto2);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(Arrays.asList(accountDto1, accountDto2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void retrieveAccountById_Basic() throws Exception {
        User user = new User(1L, "Lisa"); //mock(User.class);
        UserDto userDto = new UserDto(1L, "Lisa"); //mock(UserDto.class);

        Account account1 = new Account(user, "1234", "6721613755520555");
        account1.setId(44L);

        AccountDto accountDto1 = new AccountDto(44L, "6721613755520555", userDto, BigDecimal.ZERO);

        when(accountService.findById(9L)).thenReturn(account1);
        when(accountMapper.mapToDto(account1)).thenReturn(accountDto1);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(accountDto1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/{id}", 9)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void retrieveAccountsByUser_Basic() throws Exception {
        User user = mock(User.class);
        UserDto userDto = new UserDto(1L, "Lisa"); //mock(UserDto.class);
        Account account1 = new Account(user, "1234", "6721613755520555");
        account1.setId(44L);
        account1.setBalance(BigDecimal.valueOf(30));
        Account account2 = new Account(user, "1234", "6721613755520939");
        account2.setId(45L);
        account2.setBalance(BigDecimal.valueOf(200));

        AccountDto accountDto1 = new AccountDto(44L, "6721613755520555", userDto, BigDecimal.ZERO);
        AccountDto accountDto2 = new AccountDto(45L, "6721613755520939", userDto, BigDecimal.ZERO);

        when(userService.findById(1L)).thenReturn(user);
        when(user.getAccounts()).thenReturn(Arrays.asList(account1,account2));

        when(accountMapper.mapToDto(user.getAccounts().get(0))).thenReturn(accountDto1);
        when(accountMapper.mapToDto(user.getAccounts().get(1))).thenReturn(accountDto2);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(Arrays.asList(accountDto1,accountDto2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/{userId}/accounts", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    void addAccount_Basic() throws Exception {
        String password = "1234";
        Account accountMock = mock(Account.class);

        AccountCreateRequest bodyRequest = new AccountCreateRequest();
        bodyRequest.setPassword(password);

        when(accountService.createAccount(1L, password)).thenReturn(accountMock);
        when(accountMock.getId()).thenReturn(330L);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(bodyRequest);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users/{userId}/accounts", 1)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteAccount_Basic() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/accounts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted account id: 1"));

        verify(accountService, times(1)).delete(1L);
    }
}