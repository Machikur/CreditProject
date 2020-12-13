package com.bank.controller;

import com.bank.client.currency.Currency;
import com.bank.client.currency.Quotes;
import com.bank.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1")
public class ClientController {

    private final CurrencyService currencyService;

    @Autowired
    public ClientController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/exchangeRates")
    public Quotes getExchangeRates(@RequestParam Currency currency) {
        return currencyService.findActualExchangeRates(currency);
    }

    @GetMapping("/exchangeQuote")
    public BigDecimal getExchangeQuote(@RequestParam Currency from, @RequestParam Currency to, @RequestParam BigDecimal quote) {
        return currencyService.findExchangeQuote(from, to, quote);
    }

}
