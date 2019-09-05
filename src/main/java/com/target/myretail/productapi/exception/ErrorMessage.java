package com.target.myretail.productapi.exception;

import lombok.Getter;

public class ErrorMessage {
    @Getter
    private final String error;

    public ErrorMessage(String error){
        this.error=error;
    }

}
