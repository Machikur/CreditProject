package com.bank.service;

import com.bank.client.currency.Currency;
import com.bank.client.currency.CurrencyClient;
import com.bank.client.currency.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyService {

    private final CurrencyClient currencyClient;

    @Autowired
    public CurrencyService(CurrencyClient currencyClient) {
        this.currencyClient = currencyClient;
    }

    public Quotes findActualExchangeRates(Currency currency) {
        return currencyClient.getActualExchangeRates(currency);
    }

    public BigDecimal findExchangeQuote(Currency from, Currency to, BigDecimal quote) {
        return quote.multiply(BigDecimal.valueOf(findExchangeRate(from, to)));
    }

    private double findExchangeRate(Currency from, Currency to) {
        if (from.equals(to)) {
            return 1.00;
        }
        return currencyClient.getActualExchangeRates(from).getRates().getRate(to);
    }

}
