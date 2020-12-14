package com.bank.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditEngineTest {

    @Autowired
    private CreditEngine creditEngine;

    @Test
    public void countInterestTest() {

        //when
        double shouldBeHigh = creditEngine.countInterest(Status.STANDARD, CreditType.ANNUAL);
        double shouldBeMedium = creditEngine.countInterest(Status.NEW, CreditType.SIX_MONTH);
        double shouldBeSmall = creditEngine.countInterest(Status.NEW, CreditType.MONTHLY);
        double shouldBeTheSmallest = creditEngine.countInterest(Status.VIP, CreditType.WEEKLY);

        //then
        assertTrue(shouldBeHigh > shouldBeMedium);
        assertTrue(shouldBeMedium > shouldBeSmall);
        assertTrue(shouldBeSmall > shouldBeTheSmallest);
    }

    @Test
    public void checkAvailableCreditsTypeForAccountTest() {

        //when
        List<CreditType> shouldBeTheSmallest = creditEngine.checkAvailableCreditsTypeByStatus(Status.STANDARD);
        List<CreditType> shouldBeMedium = creditEngine.checkAvailableCreditsTypeByStatus(Status.NEW);
        List<CreditType> shouldBeTheBiggest = creditEngine.checkAvailableCreditsTypeByStatus(Status.VIP);

        //then
        assertTrue(shouldBeTheBiggest.size() >= shouldBeMedium.size());
        assertTrue(shouldBeMedium.size() >= shouldBeTheSmallest.size());
        assertTrue(shouldBeTheBiggest.size() != shouldBeTheSmallest.size());
    }

    @Test
    public void getMaxQuoteByAccountStatusTest() {
        //when
        BigDecimal shouldBeHigh = creditEngine.getMaxQuoteByAccountStatus(Status.VIP);
        BigDecimal shouldBeMedium = creditEngine.getMaxQuoteByAccountStatus(Status.NEW);
        BigDecimal shouldBeSmall = creditEngine.getMaxQuoteByAccountStatus(Status.STANDARD);

        //then
        assertTrue(shouldBeHigh.compareTo(shouldBeMedium) > 0);
        assertTrue(shouldBeMedium.compareTo(shouldBeSmall) > 0);
        assertTrue(shouldBeHigh.compareTo(shouldBeSmall) > 0);
    }

    @Test
    public void getFinishTimeTest() {

        //when
        LocalDate afterWeek = creditEngine.getFinishTime(CreditType.WEEKLY);
        LocalDate afterMonth = creditEngine.getFinishTime(CreditType.MONTHLY);
        LocalDate afterAnnual = creditEngine.getFinishTime(CreditType.ANNUAL);

        //then
        assertEquals(0, afterWeek.compareTo(LocalDate.now().plusDays(CreditType.WEEKLY.getDays())));
        assertEquals(0, afterMonth.compareTo(LocalDate.now().plusDays(CreditType.MONTHLY.getDays())));
        assertEquals(0, afterAnnual.compareTo(LocalDate.now().plusDays(CreditType.ANNUAL.getDays())));
    }

}