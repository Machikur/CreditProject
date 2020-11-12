package com.example.credit.bank;

import java.math.BigDecimal;

public class AccountDto {
    private Long id;
    private boolean isOpen;
    private BigDecimal cashBalance;
    private int userId;
    private Currency currency;
    private String accountNumber;
}
