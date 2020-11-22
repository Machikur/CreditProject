package com.bank.service;

import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.client.Currency;
import com.bank.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditServiceIntegrationTest {

    private final static String USER_NAME = "Piotr";
    private final static String USER_LAST_NAME = "Kowalski";
    private final static String USER_EMAIL = "kowalski@Gmail.com";
    private final static Double USER_EARNINGS = 1000.0;
    @Autowired
    private UserService userService;
    @Autowired
    private CreditService creditService;

    @Test
    public void shouldNotDeleteUser() throws UserNotFoundException {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Credit credit = new Credit(user, BigDecimal.TEN, Currency.EUR, LocalDate.now().plusDays(1));
        user.getCredits().add(credit);
        userService.saveUser(user);
        Long userId = user.getId();

        //when
        creditService.deleteCredit(credit);
        boolean isExists = userService.existById(userId);

        //then
        Assert.assertTrue(isExists);

        //cleanUp
        userService.deleteUser(user);
    }

    @Test
    public void shouldRefreshUser() {
        //given
        User user = new User(USER_NAME, USER_LAST_NAME, USER_EMAIL, USER_EARNINGS);
        Credit credit = new Credit(user, BigDecimal.TEN, Currency.EUR, LocalDate.now().plusDays(1));
        user.getCredits().add(credit);
        userService.saveUser(user);

        //when
        user.setFirstName("Marek");
        creditService.saveCredit(credit);

        //then
        Assert.assertNotEquals(credit.getUser().getFirstName(), USER_NAME);
    }
}