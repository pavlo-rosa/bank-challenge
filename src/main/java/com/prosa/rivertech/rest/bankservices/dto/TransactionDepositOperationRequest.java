package com.prosa.rivertech.rest.bankservices.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionDepositOperationRequest implements Serializable {

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    public TransactionDepositOperationRequest() {
    }

    public TransactionDepositOperationRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
