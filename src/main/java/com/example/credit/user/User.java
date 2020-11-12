package com.example.credit.user;

import com.example.credit.bank.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3)
    @NotBlank
    private String firstName;

    @Size(min = 3)
    @NotBlank
    private String lastName;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    private String mailAdress;

    @OneToMany(
            targetEntity = Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<Account> accounts;

    @NotNull
    private Double monthlyEarnings;

    public User(String firstName, String lastName, String mailAdress, List<Account> accounts, Double monthlyEarnings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAdress = mailAdress;
        this.accounts = accounts;
        this.monthlyEarnings = monthlyEarnings;
    }
}
