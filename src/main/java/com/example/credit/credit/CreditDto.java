package com.example.credit.credit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@AllArgsConstructor
@Getter
public class CreditDto {
    private Long id;
    private Long userId;
    private BigDecimal amountToPay;
    private BigDecimal amountPaid;
    private Currency currency;
    private LocalDate finishDate;
    private LocalDate startTime;
    private boolean isFinished;
}
