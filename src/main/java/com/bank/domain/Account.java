package com.bank.domain;

import com.bank.client.currency.Currency;
import com.bank.exception.AccountOperationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "ACCOUNT")
public class Account implements PaymentDirection {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal cashBalance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private User user;

    @NotNull
    private Currency currency;

    @Pattern(regexp = "^[0-9]{2}[-\\s][0-9]{4}[-\\s][0-9]{4}[-\\s][0-9]{4}[-\\s][0-9]{4}[-\\s][0-9]{4}[-\\s][0-9]{4}$")
    @NotNull
    private String accountNumber;

    @Digits(integer = 4, fraction = 0)
    private int pinCode;

    private LocalDate createTime;

    @OneToMany(
            targetEntity = Payment.class,
            mappedBy = "accountFrom",
            cascade = CascadeType.ALL
    )
    private List<Payment> paymentsFrom;

    @OneToMany(
            targetEntity = Payment.class,
            mappedBy = "accountTo",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Payment> paymentsTo;

    public Account(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
        this.createTime = LocalDate.now();
        this.cashBalance = BigDecimal.ZERO;
        this.paymentsFrom = new ArrayList<>();
        this.paymentsTo = new ArrayList<>();
    }

    @Override
    public void depositMoney(BigDecimal quote) {
        cashBalance = cashBalance.add(quote);
    }

    public void withdrawMoney(BigDecimal quote) throws AccountOperationException {
        if (quote.compareTo(cashBalance) > 0) {
            throw new AccountOperationException("Na koncie nie ma wystraczających środków");
        } else {
            cashBalance = cashBalance.subtract(quote);
        }

    }

    private void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

}
