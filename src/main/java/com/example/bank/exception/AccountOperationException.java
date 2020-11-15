package com.example.bank.exception;

public class AccountOperationException extends Exception {
    public AccountOperationException() {
    }

    public AccountOperationException(String message) {
        super(message);
    }
}
