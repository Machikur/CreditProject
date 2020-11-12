package com.example.credit.credit;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class CreditDto {
    private Long id;
    private Long userId;
    private int creditCompanyID;
    private BigDecimal amountToPay;
    private BigDecimal amountPaid;
    private Date finalDate;
    private Currency currency;
}
