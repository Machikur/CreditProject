package com.bank.service;

import com.bank.client.currency.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditServiceTest {

    @Autowired
    private CreditService creditService;

    @Autowired
    private UserService userService;

    @Test
    public void findAllByFinishTimeBeforeNowTestTest() throws UserNotFoundException {
        //given
        User user = new User("Man", "1234", "mail@mail.com", 2000.0);
        userService.saveUser(user);

        Credit creditOne = new Credit(user, BigDecimal.TEN, Currency.PLN, LocalDate.now().minusDays(1));
        creditOne.setFinished(true);
        creditOne.setUser(user);
        creditService.saveCredit(creditOne);

        Credit creditTwo = new Credit(user, BigDecimal.TEN, Currency.PLN, LocalDate.now().minusDays(1));
        creditTwo.setFinished(false);
        creditTwo.setUser(user);
        creditService.saveCredit(creditTwo);

        Credit creditThree = new Credit(user, BigDecimal.TEN, Currency.PLN, LocalDate.now().plusDays(1));
        creditThree.setFinished(false);
        creditTwo.setUser(user);
        creditService.saveCredit(creditThree);

        Credit creditFour = new Credit(user, BigDecimal.TEN, Currency.PLN, LocalDate.now().plusDays(1));
        creditFour.setFinished(true);
        creditFour.setUser(user);
        creditService.saveCredit(creditFour);

        //when
        List<Credit> credits = creditService.findAllByFinishTimeBeforeAndIsFinished(LocalDate.now(), false);
        //then
        Assert.assertEquals(1, credits.size());

        //cleanup
        creditTwo.setFinished(true);
        creditThree.setFinished(true);
        userService.deleteUser(userService.findById(user.getId()));
    }

}
