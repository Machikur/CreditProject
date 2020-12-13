package com.bank.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mail {
    private final String mailTo;
    private final String title;
    private final String message;

}
