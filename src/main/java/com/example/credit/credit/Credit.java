package com.example.credit.credit;

import com.example.credit.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

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
    @NotNull
    private User user;

    @NotNull
    private BigDecimal amountToPay;

    private BigDecimal amountPaid = BigDecimal.ZERO;

    @NotNull
    private Currency currency;

    @NotNull
    private LocalDate finishTime;

    private LocalDate startTime = LocalDate.now();

    private boolean isFinished = false;


}
