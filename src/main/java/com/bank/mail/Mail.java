package com.bank.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mail {
    private String mailTo;
    private String title;
    private String message;
}
