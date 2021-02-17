package com.prosa.rivertech.rest.bankservices.dto;

import com.prosa.rivertech.rest.bankservices.entity.User;

import java.math.BigDecimal;

public class AccountDto {

    private Long id;
    private String number;
    private User owner;
    private BigDecimal balance;

    public AccountDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
