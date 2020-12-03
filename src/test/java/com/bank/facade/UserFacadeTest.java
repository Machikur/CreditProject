package com.bank.facade;

import com.bank.bank.Status;
import com.bank.client.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.UserDto;
import com.bank.exception.AccountOperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.exception.UserOperationException;
import com.bank.mapper.UserMapper;
import com.bank.service.UserService;
import org.junit.Assert;
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
import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserFacadeTest {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountFacade accountFacade;

    @Test
    public void saveUserTest() throws UserNotFoundException {
        //given
        User user = getSimpleUser();
        Long userId = user.getId();

        //when
        UserDto userDto = userFacade.saveUser(userMapper.mapToUserDto(user));

        //then
        Assert.assertNull(userId);
        Assert.assertNotNull(userDto.getId());
        Assert.assertEquals(user.getName(), userDto.getName());
        Assert.assertEquals(user.getPassword(), userDto.getPassword());

        //clean up
        userService.deleteUser(userService.findById(userDto.getId()));
    }

    @Test
    public void loadUserTest() throws UserNotFoundException {
        //given
        User user = getSimpleUser();
        userFacade.saveUser(userMapper.mapToUserDto(user));

        //when
        UserDto userDto = userFacade.findUserByNameAndPassword(user.getName(), user.getPassword());

        //then
        Assert.assertNotNull(userDto.getId());
        Assert.assertEquals(user.getName(), userDto.getName());
        Assert.assertEquals(user.getPassword(), userDto.getPassword());
        Assert.assertEquals(user.getMailAddress(), userDto.getMailAddress());
        Assert.assertEquals(user.getMonthlyEarnings(), userDto.getMonthlyEarnings());

        //clean up
        userService.deleteUser(userService.findById(userDto.getId()));
    }

    @Test
    public void deleteUserTest() throws UserNotFoundException, UserOperationException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        long userId = user.getId();

        //when
        userFacade.deleteUser(userId);
        boolean userIsExist = userService.existById(userId);

        //then
        Assert.assertFalse(userIsExist);

        //clean up
    }

    @Test(expected = UserOperationException.class)
    public void shouldNotDeleteUserTest() throws UserNotFoundException, UserOperationException, AccountOperationException {
        //given
        User user = getSimpleUser();
        Account account = new Account(2L, BigDecimal.TEN, user, Currency.EUR, "22 2222 2222 2222 2222 2222 2222",
                2133, LocalDate.now(), new ArrayList<>(), new ArrayList<>());
        userService.saveUser(user);
        user.getAccounts().add(account);
        userService.saveUser(user);
        long userId = user.getId();

        //when
        userFacade.deleteUser(userId);

        //then expect exception

        //clean up
        account.withdrawMoney(BigDecimal.TEN);
        userService.saveUser(user);
        userFacade.deleteUser(userId);
    }

    @Test
    public void updateUserTest() throws UserNotFoundException {
        //given
        User user = getSimpleUser();
        String oldName = user.getName();
        String oldPassword = user.getPassword();
        String oldMail = user.getMailAddress();
        double oldEarnings = user.getMonthlyEarnings();
        userService.saveUser(user);

        Long id = user.getId();
        UserDto updateUser = new UserDto(id, "Kamil", "HAAAA", "MILA@sdas.com", new ArrayList<>(), new ArrayList<>(),
                200.0, Status.STANDARD, LocalDate.now().minusDays(1L));

        //when
        userFacade.updateUser(updateUser);
        User afterUpdate = userService.findById(id);

        //then
        Assert.assertNotEquals(afterUpdate.getName(), oldName);
        Assert.assertNotEquals(afterUpdate.getPassword(), oldPassword);
        Assert.assertNotEquals(afterUpdate.getMailAddress(), oldMail);
        Assert.assertNotEquals(afterUpdate.getMonthlyEarnings(), oldEarnings, 0.1);

        //clean up
        userService.deleteUser(userService.findById(user.getId()));
    }

    @Test
    public void getListOfUsersCurrenciesTest() throws UserNotFoundException {
        //given
        User user = getSimpleUser();
        userService.saveUser(user);
        Long id = user.getId();
        accountFacade.createNewAccount(id, Currency.PLN);
        accountFacade.createNewAccount(id, Currency.EUR);
        accountFacade.createNewAccount(id, Currency.GBP);

        //when
        Collection<Currency> currencies = userFacade.getListOfUsersCurrencies(user.getId());

        //then
        Assert.assertEquals(3, currencies.size());
        Assert.assertTrue(currencies.contains(Currency.EUR));
        Assert.assertTrue(currencies.contains(Currency.PLN));
        Assert.assertTrue(currencies.contains(Currency.GBP));
        Assert.assertFalse(currencies.contains(Currency.USD));

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