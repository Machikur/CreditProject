package com.example.bank.domain;

import com.example.bank.bank.CreditType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreditOptions {
    private BigDecimal maxQuote;
    private List<CreditType> availableCreditTypes;
}
