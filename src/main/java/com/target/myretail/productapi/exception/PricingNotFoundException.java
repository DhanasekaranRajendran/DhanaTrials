package com.target.myretail.productapi.exception;

public class PricingNotFoundException extends RuntimeException {
    public PricingNotFoundException() {
        super();
    }

    public PricingNotFoundException(String message) {
        super(message);
    }
}
