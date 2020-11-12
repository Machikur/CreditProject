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

    @NotNull
    private BigDecimal cashBalance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private User user;

    @NotNull
    private Currency currency;

    @Pattern(regexp = "^[0-9]{2}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}$")
    @NotNull
    private String accountNumber;
}
