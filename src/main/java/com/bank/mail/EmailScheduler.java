package com.bank.mail;

import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class EmailScheduler {

    private final EmailService emailService;
    private final CreditService creditService;

    @Autowired
    public EmailScheduler(EmailService emailService, CreditService creditService) {
        this.emailService = emailService;
        this.creditService = creditService;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendInformationEmail() {
        List<Credit> creditList = creditService.findAllByFinishTimeBeforeAndIsFinished(LocalDate.now(), false);
        for (Credit c : creditList) {
            User user = c.getUser();
            String MESSAGE = "Drogi użytkowniku przypominam że termin spłacenia kredytu upłynął" +
                    "Prosimy o niezwłoczne uregulowanie zobowiązań";
            emailService.send(new Mail(
                    user.getMailAddress(),
                    user.getName() + " " + user.getName(),
                    MESSAGE));
            log.info("Email do {}  został wysłany", user.getName());
        }
    }

}
