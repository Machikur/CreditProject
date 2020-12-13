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

    @Qualifier("templateEngine")
    private final TemplateEngine templateEngine;
    private final AdminConfig adminConfig;

    @Autowired
    public EmailCreator(AdminConfig adminConfig, TemplateEngine templateEngine) {
        this.adminConfig = adminConfig;
        this.templateEngine = templateEngine;
    }

    public String buildEmail(Mail mail) {
        Context context = new Context();
        context.setVariable("Message", mail.getMessage());
        context.setVariable("Admin", adminConfig);
        context.setVariable("Data", LocalDateTime.now());
        return templateEngine.process("mail.html", context);
    }

}
