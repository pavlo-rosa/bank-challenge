package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.entity.Transaction;
import com.prosa.rivertech.rest.bankservices.entity.Transference;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.exception.UnauthorizedException;
import com.prosa.rivertech.rest.bankservices.repository.TransactionRepository;
import com.prosa.rivertech.rest.bankservices.repository.TransferenceRepository;
import com.prosa.rivertech.rest.bankservices.utils.AuthorizationManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;


@DataJpaTest
class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private OperationService operationService;
    @Mock
    private TransferenceRepository transferenceRepository;
    @Mock
    private AuthorizationManager authorizationManager;

    @Test
    void findAllByAccountId_Basic() {
        Transaction transaction1 = new Transaction(mock(Operation.class), mock(Account.class), new BigDecimal(50), new BigDecimal(150), null);
        transaction1.setId(1L);
        Transaction transaction2 = new Transaction(mock(Operation.class), mock(Account.class), new BigDecimal(5), new BigDecimal(5), null);
        transaction2.setId(20L);
        Account accountMock = mock(Account.class);

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(accountMock.getTransactions()).thenReturn(Arrays.asList(
                transaction1,
                transaction2
        ));

        List<Transaction> transactions = transactionService.findAllByAccountId(3L);
        assertThat(transactions).isNotNull().hasSize(2);
        assertThat(transactions.get(1)).isEqualTo(transaction1);
        assertThat(transactions.get(0)).isEqualTo(transaction2);
    }

    @Test
    void findAllByAccountId_Empty() {
        Account accountMock = mock(Account.class);

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(accountMock.getTransactions()).thenReturn(new ArrayList<Transaction>());

        List<Transaction> transactions = transactionService.findAllByAccountId(3L);

        assertThat(transactions).isNotNull().isEmpty();
    }

    @Test
    void findAllByAccountId_AccountNotFound() {
        when(accountService.findById(2L)).thenThrow(new NotFoundException("Account id not found - 2"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.findAllByAccountId(2L);
                }).withMessage("Account id not found - 2");
    }


    @Test
    void addDeposit_Basic() {
        Account accountMock = mock(Account.class);
        Operation operationMock = mock(Operation.class);

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(operationService.findById(anyInt())).thenReturn(operationMock);
        when(accountMock.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(operationMock, accountMock, new BigDecimal(1), new BigDecimal(51), null));

        Transaction transaction = transactionService.addDeposit(1L, new BigDecimal(1));

        assertThat(transaction).isNotNull();
        assertThat(transaction.getBalance()).isEqualTo(new BigDecimal(51));
        assertThat(transaction.getAmount()).isEqualTo(new BigDecimal(1));
        assertThat(transaction.getTransference()).isNull();
    }

    @Test
    void addDeposit_AccountNotFoundException() {
        when(accountService.findById(2L)).thenThrow(new NotFoundException("Account id not found - 2"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addDeposit(2L, BigDecimal.valueOf(50));
                }).withMessage("Account id not found - 2");
    }

    @Test
    void addDeposit_OperationNotFoundException() {
        Account accountMock = mock(Account.class);

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(operationService.findById(anyInt())).thenThrow(new NotFoundException("Operation id not found - 1"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addDeposit(2L, BigDecimal.valueOf(50));
                }).withMessage("Operation id not found - 1");
    }

    @Test
    void addWithdrawal_Basic() {
        Account accountMock = mock(Account.class);
        Operation operationMock = mock(Operation.class);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(authorizationManager.validateUser(authorizationMock, accountMock)).thenReturn(true);

        when(accountMock.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(operationService.findById(anyInt())).thenReturn(operationMock);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(operationMock, accountMock, new BigDecimal(-1), new BigDecimal(49), null));

        Transaction transaction = transactionService.addWithdrawal(1L, new BigDecimal(-1), authorizationMock);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getBalance()).isEqualTo(new BigDecimal(49));
        assertThat(transaction.getAmount()).isEqualTo(new BigDecimal(-1));
        assertThat(transaction.getTransference()).isNull();
    }

    @Test
    void addWithdrawal_AccountNotFoundException() {
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";
        when(accountService.findById(anyLong())).thenThrow(new NotFoundException("Account id not found - 1"));

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addWithdrawal(1L, new BigDecimal(-1), authorizationMock);
                }).withMessage("Account id not found - 1");
    }

    @Test
    void addWithdrawal_UnauthorizedException() {
        Account accountMock = mock(Account.class);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(authorizationManager.validateUser(authorizationMock, accountMock)).thenReturn(false);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> {
                    transactionService.addWithdrawal(1L, new BigDecimal(-1), authorizationMock);
                }).withMessage("Account number or Password invalid");
    }

    @Test
    void addWithdrawal_InsufficientBalanceException() {
        Account accountMock = mock(Account.class);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(authorizationManager.validateUser(authorizationMock, accountMock)).thenReturn(true);
        when(accountMock.getBalance()).thenReturn(BigDecimal.valueOf(0));

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> {
                    transactionService.addWithdrawal(2L, BigDecimal.valueOf(-1), authorizationMock);
                }).withMessage("Insufficient balance");
    }

    @Test
    void addWithdrawal_OperationNotFoundException() {
        Account accountMock = mock(Account.class);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(accountService.findById(anyLong())).thenReturn(accountMock);
        when(authorizationManager.validateUser(authorizationMock, accountMock)).thenReturn(true);
        when(accountMock.getBalance()).thenReturn(BigDecimal.valueOf(50));

        when(operationService.findById(anyInt())).thenThrow(new NotFoundException("Operation id not found - 2"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addWithdrawal(2L, BigDecimal.valueOf(-1), authorizationMock);
                }).withMessage("Operation id not found - 2");
    }

    @Test
    void addTransference_Basic() {
        Account accountMockSource = mock(Account.class);
        Account accountMockDestination = mock(Account.class);
        Operation operationMock = mock(Operation.class);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        BigDecimal amount = new BigDecimal(2);

        when(accountService.findById(1L)).thenReturn(accountMockSource);
        when(accountMockSource.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(authorizationManager.validateUser(authorizationMock, accountMockSource)).thenReturn(true);
        when(operationService.findById(anyInt())).thenReturn(operationMock);
        when(accountService.findById(2L)).thenReturn(accountMockDestination);
        when(accountMockDestination.getBalance()).thenReturn(BigDecimal.valueOf(20));

        Transference transferenceMock = new Transference(accountMockSource, accountMockDestination, amount, operationMock);
        transferenceMock.setId(88L);
        when(transferenceRepository.save(any(Transference.class))).thenReturn(transferenceMock);

        Transaction transactionSourceMock = new Transaction(operationMock, accountMockSource, amount.negate(), accountMockSource.getBalance().add(amount.negate()), transferenceMock);
        Transaction transactionSourceMockSaved = new Transaction(operationMock, accountMockSource, amount.negate(), accountMockSource.getBalance().add(amount.negate()), transferenceMock);
        transactionSourceMockSaved.setId(33L);
        when(transactionRepository.save(transactionSourceMock)).thenReturn(transactionSourceMockSaved);

        Transaction transactionDestinationMock = new Transaction(operationMock, accountMockSource, amount, accountMockSource.getBalance().add(amount), transferenceMock);
        Transaction transactionDestinationMockSaved = new Transaction(operationMock, accountMockSource, amount, accountMockSource.getBalance().add(amount), transferenceMock);
        transactionDestinationMockSaved.setId(44L);
        when(transactionRepository.save(transactionDestinationMock)).thenReturn(transactionDestinationMockSaved);

        Transaction transaction = transactionService.addTransference(1L, 2L, amount, authorizationMock);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getTransference()).isNotNull().isEqualTo(transferenceMock);
        assertThat(transaction.getAmount())
                .isEqualTo(transactionSourceMockSaved.getAmount())
                .isEqualTo(transferenceMock.getAmount().negate())
                .isEqualTo(transactionDestinationMock.getAmount().negate());
        assertThat(transaction.getBalance()).isEqualTo(transactionSourceMockSaved.getBalance());
    }

    @Test
    void addTransference_AccountsNotFoundException() {
        BigDecimal amount = new BigDecimal(2);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(accountService.findById(1L)).thenThrow(new NotFoundException("Account id not found - 1"));

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addTransference(1L, 2L, amount, authorizationMock);
                }).withMessage("Account id not found - 1");

    }

    @Test
    void addTransference_AccountDestinationNotFoundException() {
        BigDecimal amount = new BigDecimal(2);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Account accountMockSource = mock(Account.class);
        when(accountService.findById(1L)).thenReturn(accountMockSource);
        when(accountMockSource.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(authorizationManager.validateUser(authorizationMock, accountMockSource)).thenReturn(true);
        when(accountService.findById(2L)).thenThrow(new NotFoundException("Account id not found - 2"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addTransference(1L, 2L, amount, authorizationMock);
                }).withMessage("Account id not found - 2");
    }

    @Test
    void addTransference_OperationNotFoundException() {
        BigDecimal amount = new BigDecimal(2);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Account accountMockSource = mock(Account.class);
        Account accountMockDestination = mock(Account.class);
        when(accountService.findById(1L)).thenReturn(accountMockSource);
        when(accountMockSource.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(authorizationManager.validateUser(authorizationMock, accountMockSource)).thenReturn(true);
        when(accountService.findById(2L)).thenReturn(accountMockDestination);

        when(operationService.findById(anyInt())).thenThrow(new NotFoundException("Operation id not found - 3"));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    transactionService.addTransference(1L, 2L, amount, authorizationMock);
                }).withMessage("Operation id not found - 3");
    }

    @Test
    void addTransference_UnauthorizedException() {
        BigDecimal amount = new BigDecimal(2);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Account accountMockSource = mock(Account.class);
        when(accountService.findById(1L)).thenReturn(accountMockSource);
        when(accountMockSource.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(authorizationManager.validateUser(authorizationMock, accountMockSource)).thenReturn(false);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> {
                    transactionService.addTransference(1L, 2L, amount, authorizationMock);
                }).withMessage("Account number or Password invalid");
    }

    @Test
    void addTransference_InsufficientBalanceException() {
        BigDecimal amount = new BigDecimal(2);
        String authorizationMock = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        Account accountMockSource = mock(Account.class);
        when(accountService.findById(1L)).thenReturn(accountMockSource);
        when(accountMockSource.getBalance()).thenReturn(BigDecimal.valueOf(0));
        when(authorizationManager.validateUser(authorizationMock, accountMockSource)).thenReturn(true);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> {
                    transactionService.addTransference(1L, 2L, amount, authorizationMock);
                }).withMessage("Insufficient balance");
    }
}