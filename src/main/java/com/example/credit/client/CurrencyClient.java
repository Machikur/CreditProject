package com.example.credit.client;

import com.example.credit.bank.Currency;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class CurrencyClient {

    private final static String RATES_URL="https://api.exchangeratesapi.io/latest";

    @Autowired
    private RestTemplate restTemplate;

    public Quotes getActualExchangeRates(Currency currency) {
        return restTemplate.getForObject(getCurrenciesURI(currency.getDesc()), Quotes.class);
    }


    private URI getCurrenciesURI(String currency) {
        return UriComponentsBuilder.fromHttpUrl(RATES_URL)
                .queryParam("base", currency.toUpperCase())
                .build().encode().toUri();
    }
}
