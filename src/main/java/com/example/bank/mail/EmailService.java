package com.example.bank.mail;

import com.example.bank.configuration.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailCreator emailCreator;
    private final AdminConfig adminConfig;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, EmailCreator emailCreator, AdminConfig adminConfig) {
        this.javaMailSender = javaMailSender;
        this.emailCreator = emailCreator;
        this.adminConfig = adminConfig;
    }

    public void send(Mail mail) {
        try {
            javaMailSender.send(createMimeMessage(mail));
            log.info("Email has been sent");
        } catch (MailException s) {
            log.error("Failed to process email sending", s.getMessage(), s);
        }
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setText(emailCreator.buildEmail(mail), true);
        };
    }
}
