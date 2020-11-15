package com.example.bank.domain;

import com.example.bank.client.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue
    private Long paymentId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @NotNull
    private Account accountFrom;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Account accountTo;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Credit credit;

    private Currency currency;

    private LocalDateTime createTime;

    private BigDecimal quote;

    public Payment(Currency currency, BigDecimal quote) {
        this.currency = currency;
        this.quote = quote;
    }

    @PrePersist
    private void setTime() {
        createTime = LocalDateTime.now();
    }
}
