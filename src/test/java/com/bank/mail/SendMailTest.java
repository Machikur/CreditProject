package com.bank.mail;

import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.service.CreditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMailTest {

    @Autowired
    private EmailScheduler emailScheduler;

    @MockBean
    private CreditService creditService;

    @Test
    public void sendEmailToTest() {
        //sendEmailToAdmin
        Credit credit = new Credit();
        credit.setUser(new User("tak", "nie", "machikur4@gmail.com", 1220.0));
        List<Credit> credits = Collections.singletonList(credit);
        when(creditService.findAllByFinishTimeBeforeAndIsFinished(any(), anyBoolean()))
                .thenReturn(credits);
        emailScheduler.sendInformationEmail();
    }

}
