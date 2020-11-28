package com.bank.mail;

import com.bank.client.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.service.CreditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailSchedulerTest {

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Mock
    private EmailService emailService;

    @Mock
    private CreditService creditService;

    @Test
    public void shouldSendMessage() {
        //given
        Credit credit = new Credit(1L, new User(), BigDecimal.TEN, BigDecimal.ZERO,
                Currency.PLN, LocalDate.now().plusDays(1), LocalDate.now(), false, new ArrayList<>());
        when(creditService.findAllByFinishTimeBeforeNow()).thenReturn(Collections.singletonList(credit));

        emailScheduler.sendInformationEmail();

        verify(emailService, times(1)).send(any());
    }

}