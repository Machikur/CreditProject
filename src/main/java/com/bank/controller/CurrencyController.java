package com.bank.controller;

import com.bank.client.Currency;
import com.bank.client.Quotes;
import com.bank.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/exchangeRates")
    public Quotes getExchangeRates(@RequestParam Currency currency) {
        return currencyService.getActualExchangeRates(currency);
    }

    @GetMapping("/exchangeQuote")
    public BigDecimal getExchangeQuote(@RequestParam Currency from, @RequestParam Currency to, @RequestParam BigDecimal quote) {
        return currencyService.getExchangeQuote(from, to, quote);
    }

    @GetMapping("/exchangeRate")
    public double getExchangeRate(@RequestParam Currency from, @RequestParam Currency to) {
        return currencyService.getExchangeRate(from, to);
    }
}
