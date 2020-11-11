package com.example.credit.controller;

import com.example.credit.bank.Currency;
import com.example.credit.service.CurrencyService;
import com.example.credit.client.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1")
public class CurrenciesController {


    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/exchangeRates")
    public Quotes getExchangeRates(@RequestParam Currency currency) {
        return currencyService.getActualExchangeRates(currency);
    }

    @GetMapping("/exchangeQuote")
    public BigDecimal getExcahngeQuote(@RequestParam Currency from, @RequestParam Currency to, @RequestParam BigDecimal quote) {
        return currencyService.getExchangeQuote(from, to, quote);
    }

    @GetMapping("/exchangeRate")
    public double getExchangeRate(@RequestParam Currency from, @RequestParam Currency to) {
        return currencyService.getExchangeRate(from, to);
    }
}
