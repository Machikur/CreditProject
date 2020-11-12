package com.example.credit.user;

import com.example.credit.bank.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String mailAddress;
    private List<Account> accounts;
    private Double monthlyEarnings;
}
