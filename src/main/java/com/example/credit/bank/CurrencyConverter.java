package com.example.credit.bank;

import org.springframework.core.convert.converter.Converter;

public class CurrencyConverter implements Converter<String, Currency> {

    @Override
    public Currency convert(String source) {
        return Currency.valueOf(source.toUpperCase());
    }
}
