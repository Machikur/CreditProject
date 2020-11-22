package com.bank.domain;

import com.bank.client.Currency;
import com.bank.exception.AccountOperationException;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private boolean isOpen;

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
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<Payment> paymentsFrom;

    @OneToMany(
            targetEntity = Payment.class,
            mappedBy = "accountTo",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<Payment> paymentsTo;

    public Account(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
        this.isOpen = true;
        this.createTime = LocalDate.now();
        this.cashBalance = BigDecimal.ZERO;
        this.paymentsFrom = new ArrayList<>();
        this.paymentsTo = new ArrayList<>();
    }


    public void depositMoney(BigDecimal quote) {
        cashBalance = cashBalance.add(quote);
    }

    public BigDecimal withdrawMoney(BigDecimal quote) throws AccountOperationException {
        if (quote.compareTo(cashBalance) > 0) {
            throw new AccountOperationException("Na koncie nie ma wystraczających środków");
        } else {
            cashBalance = cashBalance.subtract(quote);
            return quote;
        }


    }
}
