package com.example.bank.controller;

import com.example.bank.client.Currency;
import com.example.bank.dto.AccountDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.facade.AccountFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class AccountController {

    private final AccountFacade accountFacade;

    @Autowired
    public AccountController(@RequestBody AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping("/account")
    public AccountDto createNewAccount(@RequestParam Long userId, @RequestParam Currency currency) throws UserNotFoundException {
        return accountFacade.createNewAccount(userId, currency);
    }

    @GetMapping("/account")
    public AccountDto getAccount(@RequestParam Long accountId) throws AccountNotFoundException {
        return accountFacade.getAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<AccountDto> getAccountsByUser(@RequestParam Long userId) throws UserNotFoundException {
        return accountFacade.getAccountsOfUser(userId);
    }

    @PutMapping("/deposit")
    public AccountDto depositMoney(@RequestParam Long accountId, @RequestParam BigDecimal quote) throws AccountNotFoundException {
        return accountFacade.depositMoney(accountId, quote);

    }
}
