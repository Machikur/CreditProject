package com.example.credit.creditcompany;

import com.example.credit.bank.Account;
import com.example.credit.user.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
public class CreditCompany {

    private String name;
    private double interest;
    private List<Currency> currencyOptionsList;
    private List<Status> optionalsStatus;
    private Account account;

}
