package com.prosa.rivertech.rest.bankservices.dto;

import java.util.Date;

public class ExceptionResponse {

    private int status;
    private String message;
    private Date timestamp;

    public ExceptionResponse( int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
