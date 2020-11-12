package com.example.credit.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AccountDto {
    private Long id;
    private boolean isOpen;
    private BigDecimal cashBalance;
    private Long userId;
    private Currency currency;
    private String accountNumber;
    private Status status;
    private LocalDate createTime;
}
