package com.bank.dto;

import com.bank.client.currency.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountDto {

    private Long id;

    private BigDecimal cashBalance;

    private Long userId;

    private Currency currency;

    private String accountNumber;

    private int pinCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;

    private List<PaymentDto> paymentsFrom;

    private List<PaymentDto> paymentsTo;

}
