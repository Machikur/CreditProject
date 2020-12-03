package com.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserOperationException extends Exception {

    public UserOperationException(String message) {
        super(message);
    }

}
