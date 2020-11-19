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

    public void saveAccount(Account account) {
        accountDao.save(account);
    }

    public Account findAccount(Long accountId) throws AccountNotFoundException {
        return accountDao.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public boolean existById(Long accountId) {
        return accountDao.existsById(accountId);
    }

    public void deleteAccount(Account account) {
        accountDao.delete(account);
    }


}
