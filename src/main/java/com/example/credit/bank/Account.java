package com.example.credit.bank;

import com.example.credit.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private boolean isOpen = true;

    private BigDecimal cashBalance = BigDecimal.ZERO;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private User user;

    @NotNull
    private Currency currency;

    @Pattern(regexp = "^[0-9]{2}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}$")
    @NotNull
    private String accountNumber;

    private Status status;

    private LocalDate createTime = LocalDate.now();

    public Account(User user, Currency currency, String accountNumber) {
        this.user = user;
        this.currency = currency;
        this.accountNumber = accountNumber;
        updateStatus();
    }

    public void updateStatus() {
        if (this.user.getMonthlyEarnings() > 10000) {
            status = Status.VIP;
        } else if (LocalDate.now().isBefore(createTime.plusDays(30))) {
            status = Status.NEW;
        } else status = Status.STANDARD;
    }

}
