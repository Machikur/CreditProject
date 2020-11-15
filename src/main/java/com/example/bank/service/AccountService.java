package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.repository.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {


    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account saveAccount(Account account) {
        return accountDao.save(account);
    }

    public Account findAccount(Long accountId) throws AccountNotFoundException {
        return accountDao.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public Account findAccountByAccountNumber(String accountNumber) throws AccountNotFoundException {
        return accountDao.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
    }

    public void deleteAccount(Account account) {
        accountDao.delete(account);
    }


}
