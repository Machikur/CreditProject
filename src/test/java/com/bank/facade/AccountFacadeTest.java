package com.bank.facade;

import com.bank.client.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountFacadeTest {

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Test
    public void createAccountTest() throws UserNotFoundException, AccountNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long userId = user.getId();

        //when
        AccountDto result = accountFacade.createNewAccount(userId, Currency.PLN);

        //then
        Assert.assertEquals(0, result.getCashBalance().compareTo(BigDecimal.ZERO));
        Assert.assertEquals(result.getUserId(), userId);
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertEquals(result.getPaymentsFrom(), new ArrayList<>());

        //clean up
        userService.deleteUser(userService.findById(userId));
    }

    @Test
    public void getAccountsOfUserTest() throws UserNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long userId = user.getId();
        accountFacade.createNewAccount(userId, Currency.PLN);
        accountFacade.createNewAccount(userId, Currency.GBP);
        accountFacade.createNewAccount(userId, Currency.EUR);

        //when
        List<AccountDto> accountDtoList = accountFacade.getAccountsOfUser(userId);
        boolean eurAccount = accountDtoList.stream()
                .anyMatch(a -> a.getCurrency().compareTo(Currency.EUR) == 0);
        boolean gbpAccount = accountDtoList.stream()
                .anyMatch(a -> a.getCurrency().compareTo(Currency.GBP) == 0);
        boolean plnAccount = accountDtoList.stream()
                .anyMatch(a -> a.getCurrency().compareTo(Currency.PLN) == 0);

        //then
        Assert.assertEquals(3, accountDtoList.size());
        Assert.assertTrue(eurAccount);
        Assert.assertTrue(gbpAccount);
        Assert.assertTrue(plnAccount);

        //clean up
        userService.deleteUser(userService.findById(userId));
    }

    @Test
    public void depositMoneyTest() throws UserNotFoundException, AccountNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long userId = user.getId();
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);
        Long accountId = accountDto.getId();
        Account account = accountService.findAccount(accountId);
        BigDecimal quoteBefore = account.getCashBalance();

        //when
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        BigDecimal quoteAfter = accountService.findAccount(accountId).getCashBalance();

        //then
        Assert.assertNotEquals(quoteAfter, quoteBefore);
        Assert.assertEquals(0, quoteAfter.compareTo(BigDecimal.TEN));


        //cleanUp
        userService.deleteUser(userService.findById(userId));
    }

    @Test()
    public void withdrawMoneyTest() throws UserNotFoundException, AccountNotFoundException, AccountOperationException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long userId = user.getId();
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);
        Long accountId = accountDto.getId();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        BigDecimal quoteBefore = accountService.findAccount(accountId).getCashBalance();

        //when
        accountFacade.withdrawal(accountId, BigDecimal.TEN);
        BigDecimal quoteAfter = accountService.findAccount(accountId).getCashBalance();

        //then
        Assert.assertNotEquals(quoteAfter, quoteBefore);
        Assert.assertEquals(0, quoteAfter.compareTo(BigDecimal.ZERO), 0.1);


        //cleanUp
        userService.deleteUser(userService.findById(userId));
    }

    @Test(expected = AccountOperationException.class)
    public void withdrawMoneyTestShouldThrowException() throws UserNotFoundException, AccountNotFoundException, AccountOperationException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long userId = user.getId();
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);
        Long accountId = accountDto.getId();
        accountFacade.depositMoney(accountId, BigDecimal.valueOf(5.0));
        BigDecimal quoteBefore = accountService.findAccount(accountId).getCashBalance();

        //when
        accountFacade.withdrawal(accountId, BigDecimal.TEN);
        BigDecimal quoteAfter = accountService.findAccount(accountId).getCashBalance();

        //then
        Assert.assertEquals(quoteAfter, quoteBefore);
        Assert.assertEquals(0, quoteAfter.compareTo(BigDecimal.valueOf(5.0)), 0.1);


        //cleanUp
        userService.deleteUser(userService.findById(userId));
    }

    @Test
    public void getAllCashInCurrencyTest() throws UserNotFoundException, AccountNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long id = user.getId();
        AccountDto one = accountFacade.createNewAccount(id, Currency.PLN);
        AccountDto two = accountFacade.createNewAccount(id, Currency.PLN);
        accountFacade.createNewAccount(id, Currency.GBP);

        //when
        accountFacade.depositMoney(one.getId(), BigDecimal.TEN);
        accountFacade.depositMoney(two.getId(), BigDecimal.TEN);
        Double plnOnAccount = accountFacade.getAllCashInCurrency(id, Currency.PLN);
        Double gbpOnAccount = accountFacade.getAllCashInCurrency(id, Currency.GBP);
        Double eurOnAccount = accountFacade.getAllCashInCurrency(id, Currency.EUR);

        //then
        Assert.assertEquals(20, plnOnAccount, 0.1);
        Assert.assertEquals(0, gbpOnAccount, 0.1);
        Assert.assertEquals(0, eurOnAccount, 0.1);

        //cleanUp
        userService.deleteUser(userService.findById(user.getId()));
    }

    @Test
    public void deleteUserTest() throws UserNotFoundException, AccountNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long id = user.getId();
        AccountDto one = accountFacade.createNewAccount(id, Currency.PLN);
        long accountId = one.getId();

        //when
        accountFacade.deleteAccount(accountId, one.getPinCode());
        boolean accountIsExist = accountService.existById(accountId);

        //then
        Assert.assertFalse(accountIsExist);

        //cleanUp
        userService.deleteUser(userService.findById(user.getId()));
    }

    private User getSimpleUser() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new User(generatedString, "1234", "mail@mail.com", 2000.0);
    }

}