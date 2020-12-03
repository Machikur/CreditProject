package com.bank.service;

import com.bank.client.Currency;
import com.bank.client.Quotes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Test
    public void shouldReturnDifferentQuotes() {
        //given and when
        Quotes quotesEUR = currencyService.getActualExchangeRates(Currency.EUR);
        Quotes quotesGBP = currencyService.getActualExchangeRates(Currency.GBP);
        Quotes quotesPLN = currencyService.getActualExchangeRates(Currency.PLN);
        Quotes quotesUSD = currencyService.getActualExchangeRates(Currency.USD);

        //then
        Assert.assertNotEquals(quotesEUR.getBase(), quotesGBP.getBase());
        Assert.assertNotEquals(quotesEUR.getBase(), quotesPLN.getBase());
        Assert.assertNotEquals(quotesEUR.getBase(), quotesUSD.getBase());
        Assert.assertNotEquals(quotesGBP.getBase(), quotesPLN.getBase());
    }

    @Test
    public void shouldReturnNotNullRates() {
        //given and when
        Quotes quotes = currencyService.getActualExchangeRates(Currency.PLN);

        double PLN = quotes.getRates().getRate(Currency.PLN);
        double EUR = quotes.getRates().getRate(Currency.EUR);

        //then
        Assert.assertNotNull(quotes.getRates());
        Assert.assertNotEquals(PLN, EUR, 0.1);

    }

    @Test
    public void shouldCountQuote() {
        //given
        BigDecimal quote = BigDecimal.TEN;
        BigDecimal quoteAfterChange = currencyService.getExchangeQuote(Currency.PLN, Currency.EUR, quote);


        //then
        Assert.assertTrue(quote.compareTo(quoteAfterChange) > 0);
        Assert.assertNotEquals(quote, quoteAfterChange);

    }

}