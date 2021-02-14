package com.prosa.rivertech.rest.bankservices.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum EnumOperationType {

    //    DEPOSIT(1, "deposit"),
//    WITHDRAW(2, "withdraw"),
//    TRANSFER(3, "transfer");
//
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
