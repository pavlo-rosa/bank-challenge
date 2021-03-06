package com.prosa.rivertech.rest.bankservices.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransferenceOperationRequest implements Serializable {

    @NotNull(message = "Destination must not be null")
    private Long destination;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    public TransferenceOperationRequest() {
    }

    public TransferenceOperationRequest(Long destination, BigDecimal amount) {
        this.destination = destination;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getDestination() {
        return destination;
    }

    public void setDestination(Long destination) {
        this.destination = destination;
    }
}
