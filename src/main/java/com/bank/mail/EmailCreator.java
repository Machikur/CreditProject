package com.bank.mail;

import com.bank.configuration.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
public class EmailCreator {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildEmail(Mail mail) {
        Context context = new Context();
        context.setVariable("Message", mail.getMessage());
        context.setVariable("Admin", adminConfig);
        context.setVariable("Data", LocalDateTime.now());
        return templateEngine.process("mail.html", context);
    }

}
