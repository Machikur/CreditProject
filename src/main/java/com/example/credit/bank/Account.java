package com.example.credit.bank;

import com.example.credit.customer.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private boolean isOpen;

    private BigDecimal cashBalance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;

    private Currency currency;

    private String accountNumber;
}
