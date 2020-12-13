package com.bank.facade;

import com.bank.bank.Status;
import com.bank.client.currency.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.UserDto;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserFacadeTest {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountFacade accountFacade;

    private User user;

    private long userId;

    @Before
    public void createUserForTests() {
        user = getSimpleUser();
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
    public void saveUserTest() throws UserNotFoundException {
        UserDto userDto = new UserDto(12L, "Msss", "ssss", "ms@ms.com", new ArrayList<>()
                , new ArrayList<>(), 200.0, Status.STANDARD, LocalDate.now(), true);
        //when
        userFacade.saveUser(userDto);
        UserDto result = userFacade.findUserByNameAndPassword(userDto.getName(), userDto.getPassword());
        //when & then

        Assert.assertEquals(userDto.getMailAddress(), result.getMailAddress());
        Assert.assertEquals(userDto.getMonthlyEarnings(), result.getMonthlyEarnings());
        Assert.assertEquals(userDto.getName(), result.getName());
        Assert.assertEquals(userDto.getPassword(), result.getPassword());
    }

    @Test
    public void findUserByNameAndPasswordTest() throws UserNotFoundException {
        //when
        UserDto userDto = userFacade.findUserByNameAndPassword(user.getName(), user.getPassword());

        //then
        Assert.assertNotNull(userDto.getId());
        Assert.assertEquals(user.getName(), userDto.getName());
        Assert.assertEquals(user.getPassword(), userDto.getPassword());
        Assert.assertEquals(user.getMailAddress(), userDto.getMailAddress());
        Assert.assertEquals(user.getMonthlyEarnings(), userDto.getMonthlyEarnings());
    }


    @Test
    public void deleteUserTest() throws UserNotFoundException, OperationException {
        //when
        userFacade.deleteUser(userId);
        boolean userIsExist = userService.existById(userId);

        //then
        Assert.assertFalse(userIsExist);

    }

    @Test(expected = Exception.class)
    public void shouldNotDeleteUserTest() throws UserNotFoundException, AccountOperationException, AccountNotFoundException, OperationException {
        accountFacade.createNewAccount(userId, Currency.EUR);
        Long accountId = accountFacade.getAccountsOfUser(user.getId()).get(0).getId();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        Account account = accountService.findAccount(accountId);

        //when
        userFacade.deleteUser(userId);

        //then expect exception

        //clean up
        account.withdrawMoney(BigDecimal.TEN);
        userService.saveUser(user);
    }

    @Test
    public void updateUserTest() throws UserNotFoundException {
        String oldName = user.getName();
        String oldPassword = user.getPassword();
        String oldMail = user.getMailAddress();
        double oldEarnings = user.getMonthlyEarnings();
        userService.saveUser(user);

        Long id = user.getId();
        UserDto updateUser = new UserDto(id, "Kamil", "HAAAA", "MILA@sdas.com", new ArrayList<>(), new ArrayList<>(),
                200.0, Status.STANDARD, LocalDate.now().minusDays(1L), true);

        //when
        userFacade.updateUser(updateUser);
        User afterUpdate = userService.findById(id);

        //then
        Assert.assertNotEquals(afterUpdate.getName(), oldName);
        Assert.assertNotEquals(afterUpdate.getPassword(), oldPassword);
        Assert.assertNotEquals(afterUpdate.getMailAddress(), oldMail);
        Assert.assertNotEquals(afterUpdate.getMonthlyEarnings(), oldEarnings, 0.1);
    }

    @Test
    public void getListOfUsersCurrenciesTest() throws UserNotFoundException {
        accountFacade.createNewAccount(userId, Currency.PLN);
        accountFacade.createNewAccount(userId, Currency.EUR);
        accountFacade.createNewAccount(userId, Currency.GBP);

        //when
        Collection<Currency> currencies = userFacade.getListOfUsersCurrencies(user.getId());

        //then
        Assert.assertEquals(3, currencies.size());
        Assert.assertTrue(currencies.contains(Currency.EUR));
        Assert.assertTrue(currencies.contains(Currency.PLN));
        Assert.assertTrue(currencies.contains(Currency.GBP));
        Assert.assertFalse(currencies.contains(Currency.USD));

    }

    private User getSimpleUser() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new User(generatedString, "1234", "mail@mail.com", 2000.0);
    }

}