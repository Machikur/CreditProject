package com.bank.mail;

import com.bank.client.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.service.CreditService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        Credit credit = getSimpleCredit();
        when(creditService.findAllByFinishTimeBeforeNow()).thenReturn(Collections.singletonList(credit));

        //when & then
        emailScheduler.sendInformationEmail();
        verify(emailService, times(1)).send(any());
    }

    @Test
    public void shouldSendFiveMessages() {
        //given
        List<Credit> creditList = Arrays.asList(getSimpleCredit(),
                getSimpleCredit(), getSimpleCredit(), getSimpleCredit(), getSimpleCredit());

        when(creditService.findAllByFinishTimeBeforeNow()).thenReturn(creditList);

        //when & then
        Assert.assertEquals(5, creditList.size());
        emailScheduler.sendInformationEmail();
        verify(emailService, times(5)).send(any());
    }

    @Test
    public void shouldNotSendMessages() {
        //given
        List<Credit> creditList = new ArrayList<>();

        when(creditService.findAllByFinishTimeBeforeNow()).thenReturn(creditList);

        //when & then
        Assert.assertEquals(0, creditList.size());
        emailScheduler.sendInformationEmail();
        verify(emailService, times(0)).send(any());
    }

    private Credit getSimpleCredit() {
        return new Credit(1L, new User(), BigDecimal.TEN, BigDecimal.ZERO,
                Currency.PLN, LocalDate.now().plusDays(1), LocalDate.now(), false, new ArrayList<>());
    }

}