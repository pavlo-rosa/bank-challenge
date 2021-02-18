package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.TransactionDto;
import com.prosa.rivertech.rest.bankservices.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TransactionMapperTest {

    @Test
    void mapToDto_ShouldMapTransactionToTransactionDto() {
        Operation operation = mock(Operation.class);
        Account account = mock(Account.class);
        Transference transference = mock(Transference.class);

        Transaction transaction = new Transaction(operation,  account, new BigDecimal(45), new BigDecimal(145), transference);

        when(operation.getName()).thenReturn("transference");

        TransactionDto transactionDto = TransactionMapper.INSTANCE.mapToDto(transaction);

        assertThat(transactionDto).isNotNull();
        assertThat(transactionDto.getAmount()).isEqualTo(new BigDecimal(45));
        assertThat(transactionDto.getBalance()).isEqualTo(new BigDecimal(145));
        assertThat(transactionDto.getOperation()).isEqualTo("transference");
    }

}