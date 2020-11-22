package com.bank.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminConfig {

    @Value("${admin.mail}")
    private String adminEmail;

    @Value("${admin.name}")
    private String AdminName;
}
