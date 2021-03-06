package com.bank.service;

import com.bank.client.currency.Currency;
import com.bank.domain.Account;
import com.bank.domain.Payment;
import com.bank.domain.User;
import com.bank.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceIntegrationTest {

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
    private PaymentService paymentService;

    @Test
    public void shouldNotDeleteUserTest() throws UserNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);
        Long userId = user.getId();
        System.out.println(userId);

        //when
        accountService.deleteAccount(account);
        User userToCheck = userService.findById(userId);

        //then
        Assert.assertNotNull(userToCheck);

        //cleanUp
        userService.deleteUser(userService.findById(userId));
    }

    @Test
    public void shouldNotDeletePaymentTest() throws UserNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);
        Payment payment = new Payment(null, null, null, Currency.EUR, BigDecimal.TEN);
        account.getPaymentsFrom().add(payment);
        payment.setAccountFrom(account);
        paymentService.savePayment(payment);
        accountService.saveAccount(account);

        //when
        accountService.deleteAccount(account);

        //then
        Assert.assertNotNull(payment);

        //cleanUp
        userService.deleteUser(userService.findById(user.getId()));
    }

    @Test
    public void shouldSaveUserChangesTest() throws UserNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);

        //when
        user.setName("Marek");
        accountService.saveAccount(account);

        ///then
        Assert.assertNotEquals(account.getUser().getName(), USER_NAME);

        //cleanUp

        userService.deleteUser(userService.findById(user.getId()));
    }

    @Test
    public void shouldSavePaymentChangesTest() throws UserNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Account account = new Account(user, Currency.EUR);
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setPinCode(ACCOUNT_PIN_CODE);
        user.setAccounts(Collections.singletonList(account));
        userService.saveUser(user);
        Payment payment = new Payment(null, null, null, Currency.EUR, BigDecimal.TEN);
        account.getPaymentsTo().add(payment);
        payment.setAccountFrom(account);
        payment.setAccountTo(account);
        accountService.saveAccount(account);
        paymentService.savePayment(payment);

        //when
        payment.setCurrency(Currency.GBP);
        accountService.saveAccount(account);

        //then
        Assert.assertEquals(account.getPaymentsTo().get(0).getCurrency(), Currency.GBP);

        //cleanUp
        userService.deleteUser(userService.findById(user.getId()));
    }

}