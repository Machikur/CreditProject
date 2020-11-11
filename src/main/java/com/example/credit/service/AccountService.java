package com.example.credit.service;

import com.example.credit.bank.Account;
import com.example.credit.bank.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public void saveAccount(Account account) {
        account.setAccountNumber(accountNumberCreator());
        accountDao.save(account);
    }

    public Account findAccount(Long accountId) {
        return accountDao.findById(accountId).get();
    }


    private String accountNumberCreator() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            stringBuilder.append(random.nextInt(10) - 1);
            if (i % 4 == 0) {
                stringBuilder.append("");
            }
        }
        return stringBuilder.toString();
    }
}
