package com.bank.controller;

import com.bank.aop.ToValidate;
import com.bank.aop.ValidateType;
import com.bank.client.currency.Currency;
import com.bank.dto.AccountDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.OperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.facade.AccountFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@ToValidate
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

    @GetMapping("/accounts")
    public List<AccountDto> getAccountsByUser(@RequestParam Long userId) throws UserNotFoundException {
        return accountFacade.getAccountsOfUser(userId);
    }

    @GetMapping("account/currencyCash")
    public Double getAccountsByUser(@RequestParam Long userId, Currency currency) throws UserNotFoundException {
        return accountFacade.getAllCashInCurrency(userId, currency);
    }

    @ToValidate(ValidateType.ACCOUNT)
    @PutMapping("account/deposit")
    public void depositMoney(@RequestParam Long accountId, @RequestParam BigDecimal quote) throws AccountNotFoundException {
        accountFacade.depositMoney(accountId, quote);
    }

    @ToValidate(ValidateType.ACCOUNT)
    @PutMapping("account/withdrawal")
    public void withdrawal(@RequestParam Long accountId, @RequestParam BigDecimal quote) throws AccountOperationException, AccountNotFoundException {
        accountFacade.withdrawal(accountId, quote);
    }

    @ToValidate(ValidateType.ACCOUNT)
    @DeleteMapping("/account")
    public void deleteAccount(@RequestParam Long accountId, @RequestParam int pinNumber) throws AccountNotFoundException, OperationException {
        accountFacade.deleteAccount(accountId, pinNumber);
    }

}
