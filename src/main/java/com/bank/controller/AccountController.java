package com.bank.controller;

import com.bank.client.Currency;
import com.bank.dto.AccountDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.facade.AccountFacade;
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

    @GetMapping("account/currencyCash")
    public Double getAccountsByUser(@RequestParam Long userId, Currency currency) throws UserNotFoundException {
        return accountFacade.getAllCashInCurrency(userId, currency);
    }

    @PutMapping("account/deposit")
    public boolean depositMoney(@RequestParam String accountNumber, @RequestParam BigDecimal quote) throws AccountNotFoundException {
        return accountFacade.depositMoney(accountNumber, quote);
    }

    @PutMapping("account/withdrawal")
    public boolean withdrawal(@RequestParam String accountNumber, @RequestParam BigDecimal quote) throws AccountNotFoundException, AccountOperationException {
        return accountFacade.withdrawal(accountNumber, quote);
    }

    @DeleteMapping("/account")
    public void deleteAccount(@RequestParam String accountNumber,@RequestParam int pinNumber) throws AccountNotFoundException {
        accountFacade.deleteAccount(accountNumber, pinNumber);
    }

}