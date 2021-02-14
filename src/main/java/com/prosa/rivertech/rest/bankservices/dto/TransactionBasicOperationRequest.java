package com.prosa.rivertech.rest.bankservices.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionBasicOperationRequest implements Serializable {

    private BigDecimal amount;

    public TransactionBasicOperationRequest() {
    }

    public TransactionBasicOperationRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
