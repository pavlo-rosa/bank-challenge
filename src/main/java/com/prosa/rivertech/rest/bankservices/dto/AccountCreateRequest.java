package com.prosa.rivertech.rest.bankservices.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountCreateRequest implements Serializable {

    @NotNull(message = "Password must not be null")
    @Pattern(regexp = "(^[0-9]{4}$)", message = "Password must have 4 digits")
    private String password;

    public AccountCreateRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
