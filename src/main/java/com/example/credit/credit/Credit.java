package com.example.credit.credit;

import com.example.credit.customer.Status;
import com.example.credit.customer.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Credit {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;

    private BigDecimal amountToPay;

    private BigDecimal amountPaid;

    private LocalDate finalDate;

    private Currency currency;

    private LocalDate finishTime;

    private boolean isFinished;


}
