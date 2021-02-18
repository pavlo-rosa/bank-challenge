package com.prosa.rivertech.rest.bankservices.utils;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorizationManagerTest {

    @Mock(lenient = true)
    private Account account;

    private AuthorizationManager authorizationManager;

    @BeforeEach
    public void before() {
        authorizationManager = new AuthorizationManager();
    }

    @Test
    void validateUser_Basic() {
        //Number account: 8701613677874974  | Pass: 5631
        String authorization = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";

        when(account.getNumber()).thenReturn("8701613677874974");
        when(account.getPassword()).thenReturn("$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna");

        boolean result = authorizationManager.validateUser(authorization, account);
        assertThat(result).isTrue();
    }

    @Test
    void validateUser_IncorrectPassword() {
        //Number account: 8701613677874974  | Pass: 5631
        String authorization = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjM5"; // ---> Pass wrong (5639)

        when(account.getNumber()).thenReturn("8701613677874974");
        when(account.getPassword()).thenReturn("$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna");

        boolean result = authorizationManager.validateUser(authorization, account);
        assertThat(result).isFalse();
    }

    @Test
    void validateUser_IncorrectUsername() {
        //Number account: 8701613677874974  | Pass: 5631
        String authorization = "Basic ODcwMTYxMzY3Nzg3MDAwMTo1NjMx"; // ---> Number account wrong (8701613677870001)

        when(account.getNumber()).thenReturn("8701613677874974");
        when(account.getPassword()).thenReturn("$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna");

        boolean result = authorizationManager.validateUser(authorization, account);
        assertThat(result).isFalse();
    }

    @Test
    void validateUser_AuthorizationEmptyOrNull() {
        //Number account: 8701613677874974  | Pass: 5631

        when(account.getNumber()).thenReturn("8701613677874974");
        when(account.getPassword()).thenReturn("$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna");

        boolean result = true;
        result = authorizationManager.validateUser("", account);
        assertThat(result).isFalse();

        result = authorizationManager.validateUser(null, account);
        assertThat(result).isFalse();
    }

    @Test
    void validateUser_AccountNullProperties() {
        //Number account: 8701613677874974  | Pass: 5631
        String authorization = "Basic ODcwMTYxMzY3Nzg3NDk3NDo1NjMx";
        boolean result = true;

        when(account.getNumber()).thenReturn(null);
        when(account.getPassword()).thenReturn("$2a$10$WpvZx6CBGe78AEOF9sgUH.Y2OiM0Pi0.cFGM/vRFypgi/4/YW1Mna");
        result = authorizationManager.validateUser(authorization, account);
        assertThat(result).isFalse();


        when(account.getNumber()).thenReturn("8701613677874974");
        when(account.getPassword()).thenReturn(null);
        result = authorizationManager.validateUser(authorization, account);
        assertThat(result).isFalse();
    }
}