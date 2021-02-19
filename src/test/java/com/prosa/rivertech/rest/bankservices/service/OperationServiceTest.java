package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Operation;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import com.prosa.rivertech.rest.bankservices.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class OperationServiceTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private OperationRepository operationRepository;

    @Test
    void findById_Basic() {
        Optional<Operation> mockOptionalOperation = Optional.of(new Operation("deposit"));
        mockOptionalOperation.get().setId(1);
        mockOptionalOperation.get().setCreatedDate(new Date());
        mockOptionalOperation.get().setUpdatedDate(new Date());

        when(operationRepository.findById(1)).thenReturn(mockOptionalOperation);

        Operation operation = operationService.findById(1);

        assertThat(operation).isNotNull();
        assertThat(operation).isEqualTo(mockOptionalOperation.get());
    }

    @Test
    void findById_OperationNotFound() {
        Optional<Operation> mockOptionalOperation = Optional.empty();

        when(operationRepository.findById(1)).thenReturn(mockOptionalOperation);
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    operationService.findById(1);
                }).withMessage("Operation id not found - 1");
    }
}