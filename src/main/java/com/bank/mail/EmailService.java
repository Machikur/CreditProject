package com.bank.mail;

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

    @Autowired
    public EmailService(JavaMailSender javaMailSender, EmailCreator emailCreator) {
        this.javaMailSender = javaMailSender;
        this.emailCreator = emailCreator;
    }

    public void send(Mail mail) {
        try {
            javaMailSender.send(createMimeMessage(mail));
            log.info("Email został wysłany");
        } catch (MailException s) {
            log.error("Błąd wysyłania, przyczyna:{}", s.getMessage(), s);
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
