package com.example.bank.dto;

import com.example.bank.client.Currency;
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
public class CreditDto {

    private Long creditId;

    private Long userId;

    private BigDecimal amountToPay;

    private BigDecimal amountPaid;

    private Currency currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    private boolean isFinished;

    private List<PaymentDto> payments;
}
