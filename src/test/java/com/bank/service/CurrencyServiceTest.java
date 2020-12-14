package com.bank.service;

import com.bank.client.currency.Currency;
import com.bank.client.currency.Quotes;
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
    public void shouldReturnDifferentQuotesTest() {
        //given & when
        Quotes quotesEUR = currencyService.findActualExchangeRates(Currency.EUR);
        Quotes quotesGBP = currencyService.findActualExchangeRates(Currency.GBP);
        Quotes quotesPLN = currencyService.findActualExchangeRates(Currency.PLN);
        Quotes quotesUSD = currencyService.findActualExchangeRates(Currency.USD);

        //then
        Assert.assertNotEquals(quotesEUR.getBase(), quotesGBP.getBase());
        Assert.assertNotEquals(quotesEUR.getBase(), quotesPLN.getBase());
        Assert.assertNotEquals(quotesEUR.getBase(), quotesUSD.getBase());
        Assert.assertNotEquals(quotesGBP.getBase(), quotesPLN.getBase());
    }

    @Test
    public void shouldReturnNotNullRatesTest() {
        //given & when
        Quotes quotes = currencyService.findActualExchangeRates(Currency.PLN);

        double PLN = quotes.getRates().getRate(Currency.PLN);
        double EUR = quotes.getRates().getRate(Currency.EUR);

        //then
        Assert.assertNotNull(quotes.getRates());
        Assert.assertNotEquals(PLN, EUR, 0.1);

    }

    @Test
    public void shouldCountQuoteTest() {
        //given
        BigDecimal quote = BigDecimal.TEN;
        BigDecimal quoteAfterChange = currencyService.findExchangeQuote(Currency.PLN, Currency.EUR, quote);


        //then
        Assert.assertTrue(quote.compareTo(quoteAfterChange) > 0);
        Assert.assertNotEquals(quote, quoteAfterChange);

    }

}