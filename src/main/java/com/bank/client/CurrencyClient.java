package com.bank.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class CurrencyClient {

    private final static String RATES_URL = "https://api.exchangeratesapi.io/latest";
    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Quotes getActualExchangeRates(Currency currency) {
        return restTemplate.getForObject(getCurrenciesURI(currency), Quotes.class);
    }

    private URI getCurrenciesURI(Currency currency) {
        if (currency.equals(Currency.EUR)) {
            return UriComponentsBuilder.fromHttpUrl(RATES_URL).build().encode().toUri();
        }
        return UriComponentsBuilder.fromHttpUrl(RATES_URL)
                .queryParam("base", currency)
                .build().encode().toUri();
    }

}
