package com.example.bank.exception;

public class PaymentCreateException extends Exception {
    public PaymentCreateException() {
    }

    public PaymentCreateException(String message) {
        super(message);
    }
}
