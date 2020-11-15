package com.example.bank.domain;

import com.example.bank.bank.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER")
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
    private String mailAddress;

    @OneToMany(
            targetEntity = Account.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(
            targetEntity = Credit.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Credit> credits;

    @NotNull
    @Range(min = 0)
    private Double monthlyEarnings;

    private Status status;

    private LocalDate createDate;

    public User(String firstName, String lastName, String mailAddress, Double monthlyEarnings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.monthlyEarnings = monthlyEarnings;
    }

    public void updateStatus() {
        if (this.monthlyEarnings > 10000) {
            this.status = Status.VIP;
        } else if (LocalDate.now().isBefore(createDate.plusDays(30))) {
            this.status = Status.NEW;
        } else status = Status.STANDARD;
    }

    @PrePersist
    private void setStartFields() {
        this.accounts = new ArrayList<>();
        this.credits = new ArrayList<>();
        this.createDate = LocalDate.now();
        updateStatus();
    }


}
