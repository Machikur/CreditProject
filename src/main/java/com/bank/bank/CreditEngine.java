package com.bank.bank;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class CreditEngine {

    private static final double STANDARD_INTEREST = 10;

    public double countInterest(Status status, CreditType creditType) {
        double creditTypeMultiplier = creditType.getDays() / 10f;
        return (STANDARD_INTEREST * status.getInterestMultiplier()) + creditTypeMultiplier;
    }

    public List<CreditType> checkAvailableCreditsTypeForAccount(Status status) {
        switch (status) {
            case VIP:
                return Arrays.asList(CreditType.values());
            case NEW:
                return Arrays.asList(CreditType.MONTHLY, CreditType.WEEKLY, CreditType.SIX_MONTH);
            default:
                return Arrays.asList(CreditType.MONTHLY, CreditType.WEEKLY);
        }
    }

    public BigDecimal getMaxQuoteByAccountStatus(Status status) {
        switch (status) {
            case VIP:
                return BigDecimal.valueOf(50000);
            case NEW:
                return BigDecimal.valueOf(10000);
            default:
                return BigDecimal.valueOf(5000);
        }
    }

    public LocalDate getFinishTime(CreditType creditType) {
        return LocalDate.now().plusDays(creditType.getDays());
    }
}
