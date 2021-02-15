package com.prosa.rivertech.rest.bankservices.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumOperationType {

    DEPOSIT(1), WITHDRAW(2), TRANSFER(3);

    private Integer id;

    private EnumOperationType(Integer id) {
        this.id = id;
    }

    @JsonValue
    public Integer getId() {
        return id;
    }

}
