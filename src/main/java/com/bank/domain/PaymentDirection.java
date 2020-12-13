package com.bank.domain;

import com.bank.client.currency.Currency;

import java.math.BigDecimal;

public interface PaymentDirection {

    Currency getCurrency();

    Long getId();

    void depositMoney(BigDecimal amount);

}
