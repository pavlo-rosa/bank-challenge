package com.prosa.rivertech.rest.bankservices.mapper;

import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AccountMapperTest {

    @Test
    void mapToDto_ShouldMapAccountToAccountDto() {
        User user = mock(User.class);
        BigDecimal balance = new BigDecimal(50);
        Account account = new Account(user,  "1234", "8765920561469854");
        account.setId(44L);
        account.setBalance(balance);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Mr. Burns");

        AccountDto accountDto = AccountMapper.INSTANCE.mapToDto(account);

        assertThat(accountDto).isNotNull();
        assertThat(accountDto.getId()).isEqualTo(44L);
        assertThat(accountDto.getBalance()).isEqualTo(balance);
        assertThat(accountDto.getNumber()).isEqualTo("8765920561469854");
        assertThat(accountDto.getOwner().getId()).isEqualTo(1L);
        assertThat(accountDto.getOwner().getName()).isEqualTo("Mr. Burns");
    }

}