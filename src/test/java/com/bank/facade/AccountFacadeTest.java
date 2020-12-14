package com.bank.facade;

import com.bank.client.currency.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.OperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

    private long userId;

    @Before
    public void createUserForTests() {
        User user = getSimpleUser();
        userService.saveUser(user);
        userId = user.getId();
    }

    @After
    public void deleteUserAndCleanUpAfterTest() {
        try {
            userService.deleteUser(userService.findById(userId));
        } catch (UserNotFoundException s) {
            System.out.println("nie znaleziono użytkownika");
        }
    }

    @Test
    public void createAccountTest() throws UserNotFoundException {
        //when
        AccountDto result = accountFacade.createNewAccount(userId, Currency.PLN);

        //then
        Assert.assertEquals(0, result.getCashBalance().compareTo(BigDecimal.ZERO));
        Assert.assertEquals((long) result.getUserId(), userId);
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertEquals(result.getPaymentsFrom(), new ArrayList<>());
    }

    @Test
    public void getAccountsOfUserTest() throws UserNotFoundException {
        //given
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
    }

    @Test
    public void depositMoneyTest() throws UserNotFoundException, AccountNotFoundException {
        //given
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
    }

    @Test()
    public void withdrawMoneyTest() throws UserNotFoundException, AccountNotFoundException, AccountOperationException {
        //given
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
    }

    @Test(expected = AccountOperationException.class)
    public void withdrawMoneyTestShouldThrowExceptionTest() throws UserNotFoundException, AccountNotFoundException, AccountOperationException {
        //given
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
    }

    @Test
    public void getAllCashInCurrencyTest() throws UserNotFoundException, AccountNotFoundException {
        //given
        AccountDto one = accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto two = accountFacade.createNewAccount(userId, Currency.PLN);
        accountFacade.createNewAccount(userId, Currency.GBP);

        //when
        accountFacade.depositMoney(one.getId(), BigDecimal.TEN);
        accountFacade.depositMoney(two.getId(), BigDecimal.TEN);
        Double plnOnAccount = accountFacade.getAllCashInCurrency(userId, Currency.PLN);
        Double gbpOnAccount = accountFacade.getAllCashInCurrency(userId, Currency.GBP);
        Double eurOnAccount = accountFacade.getAllCashInCurrency(userId, Currency.EUR);

        //then
        Assert.assertEquals(20, plnOnAccount, 0.1);
        Assert.assertEquals(0, gbpOnAccount, 0.1);
        Assert.assertEquals(0, eurOnAccount, 0.1);
    }

    @Test
    public void deleteUserTest() throws UserNotFoundException, AccountNotFoundException, OperationException {
        //given
        AccountDto one = accountFacade.createNewAccount(userId, Currency.PLN);
        long accountId = one.getId();

        //when
        accountFacade.deleteAccount(accountId, one.getPinCode());
        boolean accountIsExist = accountService.existById(accountId);

        //then
        Assert.assertFalse(accountIsExist);
    }

    @Test(expected = Exception.class)
    public void shouldNotDeleteUserTest() throws UserNotFoundException, AccountNotFoundException, AccountOperationException, OperationException {
        //given
        AccountDto one = accountFacade.createNewAccount(userId, Currency.PLN);
        long accountId = one.getId();

        //when
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        accountFacade.deleteAccount(accountId, one.getPinCode());
        boolean accountIsExist = accountService.existById(accountId);

        //then
        Assert.assertFalse(accountIsExist);

        //cleanUp
        accountFacade.withdrawal(accountId, BigDecimal.TEN);
    }

    private User getSimpleUser() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new User(generatedString, "1234", "mail@mail.com", 2000.0);
    }

}