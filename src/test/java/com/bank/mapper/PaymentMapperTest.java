package com.bank.mapper;

import com.bank.client.Currency;
import com.bank.domain.Account;
import com.bank.domain.Payment;
import com.bank.dto.PaymentDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentMapperTest {

    @Autowired
    private PaymentMapper paymentMapper;

    @Test
    public void shouldReturnPaymentDto() {
        //given
        Account accountOne = new Account();
        accountOne.setId(1L);
        Account accountTwo = new Account();
        accountTwo.setId(2L);
        Payment payment = new Payment(Currency.PLN, BigDecimal.TEN);
        payment.setAccountFrom(accountOne);
        payment.setAccountTo(accountTwo);
        payment.setCreateTime(LocalDateTime.of(1, 1, 1, 1, 1, 1));

        //when
        PaymentDto result = paymentMapper.mapToPaymentDto(payment);

        //then
        Assert.assertEquals(result.getAccountFromId(), accountOne.getId());
        Assert.assertEquals(result.getAccountToId(), accountTwo.getId());
        Assert.assertEquals(result.getCreateTime(), LocalDateTime.of(1, 1, 1, 1, 1, 1));
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertEquals(result.getQuote(), BigDecimal.TEN);
    }

    @Test
    public void shouldReturnPaymentTest() {
        //given
        PaymentDto paymentDto = new PaymentDto(1L, 1L, 1L,
                null, Currency.PLN, null, BigDecimal.TEN);

        //when
        Payment result = paymentMapper.mapToPayment(paymentDto);

        //then
        Assert.assertEquals(result.getCurrency(), paymentDto.getCurrency());
        Assert.assertEquals(result.getQuote(), paymentDto.getQuote());
    }

    @Test
    public void shouldReturnSameSizeList() {
        //given
        Account accountOne = new Account();
        accountOne.setId(1L);
        Account accountTwo = new Account();
        accountTwo.setId(2L);
        Payment payment = new Payment(Currency.PLN, BigDecimal.TEN);
        payment.setAccountFrom(accountOne);
        payment.setAccountTo(accountTwo);
        payment.setCreateTime(LocalDateTime.of(1, 1, 1, 1, 1, 1));

        List<Payment> payments = Arrays.asList(payment, payment, payment);
        int size = payments.size();

        //when
        List<PaymentDto> result = paymentMapper.mapToDtoList(payments);

        //then
        Assert.assertEquals(result.size(), size);
    }

}