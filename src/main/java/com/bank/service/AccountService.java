package com.bank.service;

import com.bank.domain.Account;
import com.bank.repository.AccountDao;
import com.bank.exception.AccountNotFoundException;
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
