package com.example.credit.customer;

import com.example.credit.bank.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    private String mail;
    @OneToMany(
            targetEntity = Account.class,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )
    private List<Account> accounts;

    private Double monthlyEarnings;

    public User(String firstName, String lastName, String mail, List<Account> accounts, Double monthlyEarnings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.accounts = accounts;
        this.monthlyEarnings = monthlyEarnings;
    }
}
