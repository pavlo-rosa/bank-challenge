package com.prosa.rivertech.rest.bankservices.dto;

import javax.validation.constraints.Negative;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionWithdrawalOperationRequest implements Serializable {

    @NotNull(message = "Amount must not be null")
    @Negative(message = "Amount must be less than 0")
    private BigDecimal amount;

    public TransactionWithdrawalOperationRequest() {
    }

    public TransactionWithdrawalOperationRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
