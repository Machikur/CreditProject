package com.bank.domain;

import com.bank.bank.Status;
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
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3)
    @NotBlank
    private String name;

    @Size(min = 3)
    @NotBlank
    private String password;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    private String mailAddress;

    @OneToMany(
            targetEntity = Account.class,
            mappedBy = "user",
            cascade = CascadeType.ALL)
    private List<Account> accounts;

    @OneToMany(
            targetEntity = Credit.class,
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Credit> credits;

    @NotNull
    @Range(min = 0)
    private Double monthlyEarnings;

    private Status status;

    private LocalDate createDate;

    public User(String name, String password, String mailAddress, Double monthlyEarnings) {
        this.name = name;
        this.password = password;
        this.mailAddress = mailAddress;
        this.monthlyEarnings = monthlyEarnings;
        this.accounts = new ArrayList<>();
        this.credits = new ArrayList<>();
        this.createDate = LocalDate.now();
        updateStatus();
    }

    @PreUpdate
    public void updateStatus() {
        if (this.monthlyEarnings > 10000) {
            this.status = Status.VIP;
        } else if (LocalDate.now().isBefore(createDate.plusDays(30))) {
            this.status = Status.NEW;
        } else status = Status.STANDARD;
    }

}
