package com.bank.mapper;

import com.bank.client.currency.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.dto.CreditDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditMapperTest {

    @Autowired
    private CreditMapper creditMapper;

    @Test
    public void shouldReturnCreditDtoTest() {
        //given
        User user = new User();
        user.setId(1L);
        Credit credit = new Credit(user, BigDecimal.TEN,
                Currency.PLN, LocalDate.now().plusDays(1));

        //when
        CreditDto result = creditMapper.mapToCreditDto(credit);

        //then
        Assert.assertEquals(result.getAmountPaid(), credit.getAmountPaid());
        Assert.assertEquals(result.getAmountToPay(), credit.getAmountToPay());
        Assert.assertEquals(result.getCurrency(), credit.getCurrency());
        Assert.assertEquals(result.getFinishTime(), credit.getFinishTime());
        Assert.assertEquals(result.getPayments().size(), credit.getPaymentsFrom().size());
        Assert.assertEquals(result.getUserId(), credit.getUser().getId());
    }

    @Test
    public void shouldReturnCreditTest() {
        //given
        User user = new User();
        user.setId(1L);
        CreditDto creditDto = new CreditDto(1L, 1L, BigDecimal.TEN, BigDecimal.ZERO,
                Currency.PLN, LocalDate.now().plusDays(1), LocalDate.now(), false, new ArrayList<>());

        //when
        Credit result = creditMapper.mapToCredit(creditDto, user);

        //then
        Assert.assertEquals(result.getAmountToPay(), creditDto.getAmountToPay());
        Assert.assertEquals(result.getCurrency(), creditDto.getCurrency());
        Assert.assertEquals(result.getFinishTime(), creditDto.getFinishTime());
        Assert.assertEquals(result.getUser(), user);
    }

    @Test
    public void shouldReturnListOfCreditDtoTest() {
        //given
        User user = new User();
        user.setId(1L);
        Credit credit = new Credit(user, BigDecimal.TEN,
                Currency.PLN, LocalDate.now().plusDays(1));
        List<Credit> credits = Arrays.asList(credit, credit, credit);

        //when
        List<CreditDto> result = creditMapper.mapToListDto(credits);

        //then
        Assert.assertEquals(result.size(), credits.size());
    }

}