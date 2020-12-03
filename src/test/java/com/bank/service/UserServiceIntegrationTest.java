package com.bank.service;

import com.bank.client.Currency;
import com.bank.domain.Account;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CreditNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

    private final static String USER_NAME = "Piotr";
    private final static String USER_LAST_NAME = "Kowalski";
    private final static String USER_EMAIL = "kowalski@Gmail.com";
    private final static Double USER_EARNINGS = 1000.0;
    private final static String ACCOUNT_NUMBER = "11 0000 0055 0022 2100 0000 0000";
    private final static int ACCOUNT_PIN_CODE = 1111;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CreditService creditService;

    @Test
    public void shouldSaveUserAndAccount() {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);

        //when
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);

        //then
        Assert.assertNotNull(account.getId());
        Assert.assertNotNull(user.getId());

        //cleanUp
        accountService.deleteAccount(account);
        userService.deleteUser(user);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldDeleteUserAndAccount() throws AccountNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);
        Long accountId = account.getId();

        //when
        userService.deleteUser(user);
        boolean isExists = accountService.existById(accountId);

        //then expect AccountNotFoundException
        Assert.assertFalse(isExists);
        accountService.findAccount(accountId);
    }

    @Test
    public void shouldSaveUserAndCredit() {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Credit credit = new Credit(user, BigDecimal.TEN, Currency.EUR, LocalDate.now().plusDays(1));
        user.getCredits().add(credit);

        //when
        userService.saveUser(user);


        //then
        Assert.assertNotNull(credit.getId());
        Assert.assertNotNull(user.getId());

        //cleanUp
        creditService.deleteCredit(credit);
        userService.deleteUser(user);
    }

    @Test(expected = CreditNotFoundException.class)
    public void shouldDeleteCredit() throws CreditNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Credit credit = new Credit(user, BigDecimal.TEN, Currency.EUR, LocalDate.now().plusDays(1));
        user.getCredits().add(credit);
        userService.saveUser(user);
        Long creditId = credit.getId();

        //when
        userService.deleteUser(user);

        //then expect Exception
        creditService.getCredit(creditId);
    }

}