package com.bank.mail;

import com.bank.client.currency.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.service.CreditService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EmailSchedulerTest {

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Mock
    private EmailService emailService;

    @Mock
    private CreditService creditService;

    @Test
    public void shouldSendMessageTest() {
        //given
        Credit credit = getSimpleCredit();
        when(creditService.findAllByFinishTimeBeforeAndIsFinished(any(), anyBoolean())).thenReturn(Collections.singletonList(credit));

        //when & then
        emailScheduler.sendInformationEmail();
        verify(emailService, times(1)).send(any());
    }

    @Test
    public void shouldSendFiveMessagesTest() {
        //given
        List<Credit> creditList = Arrays.asList(getSimpleCredit(),
                getSimpleCredit(), getSimpleCredit(), getSimpleCredit(), getSimpleCredit());

        when(creditService.findAllByFinishTimeBeforeAndIsFinished(any(), anyBoolean())).thenReturn(creditList);

        //when & then
        Assert.assertEquals(5, creditList.size());
        emailScheduler.sendInformationEmail();
        verify(emailService, times(5)).send(any());
    }

    @Test
    public void shouldNotSendMessagesTest() {
        //given
        List<Credit> creditList = new ArrayList<>();

        when(creditService.findAllByFinishTimeBeforeAndIsFinished(any(), anyBoolean())).thenReturn(creditList);

        //when & then
        Assert.assertEquals(0, creditList.size());
        emailScheduler.sendInformationEmail();
        verify(emailService, times(0)).send(any());
    }

    private Credit getSimpleCredit() {
        return new Credit(new User(), BigDecimal.TEN,
                Currency.PLN, LocalDate.now().minusDays(1));
    }

}