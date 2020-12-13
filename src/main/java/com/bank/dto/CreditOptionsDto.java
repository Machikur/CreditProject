package com.bank.dto;

import com.bank.bank.CreditType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreditOptionsDto {

    private final BigDecimal maxQuote;

    private final List<CreditType> availableCreditTypes;

}
