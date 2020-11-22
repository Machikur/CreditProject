package com.bank.exception;

public class UserOperationException extends Exception {
    public UserOperationException() {
    }

    public UserOperationException(String message) {
        super(message);
    }
}
