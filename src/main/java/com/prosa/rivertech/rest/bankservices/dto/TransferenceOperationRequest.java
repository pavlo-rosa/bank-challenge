package com.prosa.rivertech.rest.bankservices.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferenceOperationRequest implements Serializable {

    private Long destination;
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
