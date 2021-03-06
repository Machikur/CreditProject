package com.bank.domain;

import com.bank.client.currency.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "CREDIT")
public class Credit implements PaymentDirection {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private User user;

    @NotNull
    private BigDecimal amountToPay;

    private BigDecimal amountPaid;

    @NotNull
    private Currency currency;

    @NotNull
    private LocalDate finishTime;

    private LocalDate startTime;

    private boolean isFinished;

    @OneToMany(
            targetEntity = Payment.class,
            mappedBy = "credit",
            cascade = CascadeType.ALL
    )
    private List<Payment> paymentsFrom;

    public Credit(User user, BigDecimal amountToPay, Currency currency, LocalDate finishTime) {
        this.user = user;
        this.amountToPay = amountToPay;
        this.currency = currency;
        this.finishTime = finishTime;
        this.paymentsFrom = new ArrayList<>();
        this.amountPaid = BigDecimal.ZERO;
        this.startTime = LocalDate.now();
        this.isFinished = false;
    }

    @Override
    public void depositMoney(BigDecimal amount) {
        amountPaid = amountPaid.add(amount);
        if (amountToPay.compareTo(amountPaid) <= 0) {
            isFinished = true;
        }
    }

}
