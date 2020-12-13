package com.bank.facade;

import com.bank.bank.CreditType;
import com.bank.client.currency.Currency;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import com.bank.dto.CreditDto;
import com.bank.dto.CreditOptionsDto;
import com.bank.exception.*;
import com.bank.service.CreditService;
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
import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditFacadeTest {

    @Autowired
    private CreditFacade creditFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private AccountFacade accountFacade;

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
    public void createCreditForUserTest() throws UserNotFoundException, AccountNotFoundException, CreditCreateException {
        //given
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);

        //when
        creditFacade.createCreditForUser(userId, accountDto.getId(), BigDecimal.valueOf(1000), CreditType.WEEKLY);
        List<CreditDto> creditList = creditFacade.getCreditsForUser(userId);

        //then
        Assert.assertFalse(creditList.isEmpty());
    }

    @Test(expected = CreditCreateException.class)
    public void createCreditForUserExpectExceptionTest() throws UserNotFoundException, AccountNotFoundException, CreditCreateException {
        //given
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);

        //when
        creditFacade.createCreditForUser(userId, accountDto.getId(), BigDecimal.valueOf(100000), CreditType.WEEKLY);
        List<CreditDto> creditList = creditFacade.getCreditsForUser(userId);

        //then
        Assert.assertFalse(creditList.isEmpty());
    }

    @Test(expected = Exception.class)
    public void deleteCreditTestShouldNotDelete() throws UserNotFoundException, AccountNotFoundException, CreditCreateException, CreditNotFoundException, OperationException {
        //given
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);
        creditFacade.createCreditForUser(userId, accountDto.getId(), BigDecimal.valueOf(1000), CreditType.WEEKLY);
        CreditDto testCredit = creditFacade.getCreditsForUser(userId).get(0);

        //when
        creditFacade.deleteCredit(testCredit.getCreditId());

        Credit credit = creditService.findCredit(testCredit.getCreditId());

        //then
        Assert.assertNotNull(credit);
        Assert.assertTrue(BigDecimal.valueOf(1000).compareTo(credit.getAmountToPay()) < 0);
    }

    @Test(expected = Exception.class)
    public void deleteCreditTestShouldDelete() throws UserNotFoundException, AccountNotFoundException, CreditCreateException, CreditNotFoundException, OperationException {
        //given
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);
        creditFacade.createCreditForUser(userId, accountDto.getId(), BigDecimal.valueOf(1000), CreditType.WEEKLY);
        CreditDto testCredit = creditFacade.getCreditsForUser(userId).get(0);
        Credit credit = creditService.findCredit(testCredit.getCreditId());
        long creditId = credit.getId();

        //when
        creditFacade.deleteCredit(creditId);

        //then expect exception

        //cleanUp
        credit.setFinished(true);
        creditService.saveCredit(credit);
    }

    @Test
    public void countInterestTest() throws UserNotFoundException {
        //when
        double interest = creditFacade.countInterest(userId, 7);
        //then
        Assert.assertEquals(9.7, interest, 0.2);
    }

    @Test
    public void getOptionsForUserTest() throws UserNotFoundException {
        //when
        CreditOptionsDto creditOptionsDto = creditFacade.getOptionsForUser(userId);
        //then
        Assert.assertNotNull(creditOptionsDto);
        Assert.assertFalse(creditOptionsDto.getAvailableCreditTypes().isEmpty());
        Assert.assertTrue(creditOptionsDto.getMaxQuote().longValue() > 0);
    }

    @Test
    public void getCreditsForUserTest() throws UserNotFoundException, AccountNotFoundException, CreditCreateException {
        //given
        AccountDto accountDto = accountFacade.createNewAccount(userId, Currency.PLN);

        //when
        creditFacade.createCreditForUser(userId, accountDto.getId(), BigDecimal.valueOf(1000), CreditType.WEEKLY);
        List<CreditDto> creditList = creditFacade.getCreditsForUser(userId);
        CreditDto testCredit = creditList.get(0);

        //then
        Assert.assertEquals(userId, (long) testCredit.getUserId());
        Assert.assertEquals(LocalDate.now().plusDays(7), testCredit.getFinishTime());
        Assert.assertNotEquals(BigDecimal.ZERO, testCredit.getAmountPaid());
        Assert.assertNotEquals(BigDecimal.ZERO, testCredit.getAmountToPay());
        Assert.assertNotEquals(BigDecimal.valueOf(1000), testCredit.getAmountToPay());
    }

    private User getSimpleUser() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new User(generatedString, "1234", "mail@mail.com", 2000.0);
    }

}