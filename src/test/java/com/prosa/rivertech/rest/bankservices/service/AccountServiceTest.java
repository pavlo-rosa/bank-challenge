package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.BadRequestException;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import com.prosa.rivertech.rest.bankservices.repository.UserRepository;
import com.prosa.rivertech.rest.bankservices.utils.GeneratorAccountsNumberManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserService userService;
    @Mock
    private GeneratorAccountsNumberManager generatorAccountsNumberManager;


    @Test
    void findAll_Basic() {
        User user = mock(User.class);
        Account account1 = new Account(user, "1234", "9988776655443322");
        account1.setCreatedDate(new Date());
        account1.setUpdatedDate(new Date());

        Account account2 = new Account(user, "5678", "2233445566778899");
        account2.setCreatedDate(new Date());
        account2.setUpdatedDate(new Date());

        when(accountRepository.findAll()).thenReturn(Arrays.asList(
                account1,
                account2
        ));

        List<Account> accounts = accountService.findAll();

        assertThat(accounts).isNotNull();
        assertThat(accounts).hasSize(2);
        assertThat(accounts.get(0)).isEqualTo(account1);
        assertThat(accounts.get(1)).isEqualTo(account2);
    }

    @Test
    void findAll_Empty() {
        when(accountRepository.findAll()).thenReturn(new ArrayList<Account>());

        List<Account> accounts = accountService.findAll();

        assertThat(accounts).isNotNull().isEmpty();
    }

    @Test
    void findById_Basic() {
        User user = mock(User.class);
        Optional<Account> mockOptionalAccount = Optional.of(new Account(user, "1234", "9988776655443322"));
        mockOptionalAccount.get().setId(1L);
        mockOptionalAccount.get().setCreatedDate(new Date());
        mockOptionalAccount.get().setUpdatedDate(new Date());

        when(accountRepository.findById(1L)).thenReturn(mockOptionalAccount);

        Account account = accountService.findById(1L);

        assertThat(account).isNotNull();
        assertThat(account).isEqualTo(mockOptionalAccount.get());
    }

    @Test
    void findById_AccountNotFound() {

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    accountService.findById(1L);
                }).withMessage("Account id not found - 1");
    }

    @Test
    void findById_NullIdParam() {

        when(accountRepository.findById(null)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    accountService.findById(null);
                }).withMessage("Account id not found - null");
    }

    @Test
    void createAccount_Basic() {
        String inputPass = "5631";

        User expectedUser = new User(1L, "Bart");
        Account expectedAccount = new Account(expectedUser, "$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna", "8701613677874970");
        expectedAccount.setId(13L);

        when(generatorAccountsNumberManager.generateNumber()).thenReturn("8701613677874970");
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);
        when(userService.findById(1L)).thenReturn(expectedUser);

        Account account = accountService.createAccount(1L, inputPass);

        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(account).isNotNull();
        assertThat(account).isEqualTo(expectedAccount);
    }

    @Test
    void createAccount_IncorrectParams() {
        User expectedUser = new User(1L, "Bart");

        Account expectedAccount = new Account(expectedUser, "$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna", "8701613677874970");
        expectedAccount.setId(1L);

        when(generatorAccountsNumberManager.generateNumber()).thenReturn("8701613677874970");
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);
        when(userService.findById(1L)).thenReturn(new User(1L, "Bart"));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    accountService.createAccount(1L, "");
                }).withMessage("Invalid request");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    accountService.createAccount(1L, null);
                }).withMessage("Invalid request");


        when(userService.findById(2L)).thenThrow(new NotFoundException("User id not found - 2"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    accountService.createAccount(2L, "5631");
                }).withMessage("User id not found - 2");
    }

    @Test
    void delete_Basic() {
        User user = mock(User.class);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(user, "$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna", "8701613677874970")));

        accountService.delete(1L);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).deleteById(anyLong());

    }

    @Test
    void delete_AccountNotFoundException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    accountService.delete(1L);
                }).withMessage("Account id not found - 1");

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(0)).deleteById(anyLong());

    }
}