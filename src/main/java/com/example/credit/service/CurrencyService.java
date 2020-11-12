package com.example.credit.service;

import com.example.credit.bank.Currency;
import com.example.credit.client.CurrencyClient;
import com.example.credit.client.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyService {

    @Autowired
    private CurrencyClient currencyClient;

    public Quotes getActualExchangeRates(Currency currency) {
        return currencyClient.getActualExchangeRates(currency);
    }

    public double getExchangeRate(Currency from, Currency to) {
        if (from.equals(to)) {
            return 1.00;
        }
        return currencyClient.getActualExchangeRates(from).getRates().getRate(to);
    }

    public BigDecimal getExchangeQuote(Currency from, Currency to, BigDecimal quote) {
        return quote.multiply(BigDecimal.valueOf(getExchangeRate(from, to)));
    }

}
