package com.bank.facade;

import com.bank.bank.CreditType;
import com.bank.client.currency.Currency;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import com.bank.dto.CreditDto;
import com.bank.dto.PaymentDto;
import com.bank.exception.*;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentFacadeTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private CreditFacade creditFacade;

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
    public void makeAccountPaymentTest() throws UserNotFoundException, AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        //given
        accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto accountDto = accountFacade.getAccountsOfUser(userId).get(0);
        Long accountId = accountDto.getId();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        PaymentDto paymentDto = new PaymentDto(200L, accountId, accountId, null, Currency.PLN, LocalDateTime.now(), BigDecimal.TEN);
        int pinCode = accountDto.getPinCode();

        //when
        PaymentDto result = paymentFacade.makePayment(paymentDto, pinCode);

        //then

        Assert.assertEquals(result.getAccountFromId(), accountId);
        Assert.assertNull(result.getCreditId());
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertNotEquals(result.getQuote(), BigDecimal.ZERO);
    }

    @Test
    public void makeCreditPaymentTest() throws UserNotFoundException, AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException, CreditCreateException {
        //given
        accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto accountDto = accountFacade.getAccountsOfUser(userId).get(0);
        Long accountId = accountDto.getId();
        int pinCode = accountDto.getPinCode();
        creditFacade.createCreditForUser(userId, accountId, BigDecimal.TEN, CreditType.WEEKLY);
        CreditDto creditDto = creditFacade.getCreditsForUser(userId).get(0);
        PaymentDto paymentDto = new PaymentDto(200L, accountId, null, creditDto.getCreditId(), Currency.PLN, LocalDateTime.now(), BigDecimal.TEN);


        //when
        PaymentDto result = paymentFacade.makePayment(paymentDto, pinCode);

        //then

        Assert.assertEquals(result.getAccountFromId(), accountId);
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertNotEquals(result.getQuote(), BigDecimal.ZERO);
    }

    @Test(expected = PaymentCreateException.class)
    public void makePaymentExpectExceptionTest() throws UserNotFoundException, AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        //given
        accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto accountDto = accountFacade.getAccountsOfUser(userId).get(0);
        Long accountId = accountDto.getId();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        PaymentDto paymentDto = new PaymentDto(200L, accountId, accountId, null, Currency.PLN, LocalDateTime.now(), BigDecimal.TEN);

        //when
        PaymentDto result = paymentFacade.makePayment(paymentDto, 12433);

        //then

        Assert.assertEquals(result.getAccountFromId(), accountId);
        Assert.assertNull(result.getCreditId());
        Assert.assertEquals(result.getCurrency(), Currency.PLN);
        Assert.assertNotEquals(result.getQuote(), BigDecimal.ZERO);
    }

    @Test
    public void getPaymentListOfUserTest() throws UserNotFoundException, AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        //given
        accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto accountDto = accountFacade.getAccountsOfUser(userId).get(0);
        Long accountId = accountDto.getId();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        PaymentDto paymentDto = new PaymentDto(200L, accountId, accountId, null, Currency.PLN, LocalDateTime.now(), BigDecimal.TEN);
        int pinCode = accountDto.getPinCode();

        //when
        paymentFacade.makePayment(paymentDto, pinCode);
        paymentFacade.makePayment(paymentDto, pinCode);
        List<PaymentDto> paymentDtoList = paymentFacade.getPaymentListOfUser(userId);
        boolean anyPlN = paymentDtoList.stream().anyMatch(a -> a.getCurrency().equals(Currency.PLN));
        boolean accountToId = paymentDtoList.stream().anyMatch(a -> a.getAccountToId().equals(accountId));

        //then
        Assert.assertEquals(2, paymentDtoList.size());
        Assert.assertTrue(anyPlN);
        Assert.assertTrue(accountToId);
    }


    @Test
    public void getPaymentListOfAccountTest() throws UserNotFoundException, AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        //given
        accountFacade.createNewAccount(userId, Currency.PLN);
        AccountDto accountDto = accountFacade.getAccountsOfUser(userId).get(0);
        Long accountId = accountDto.getId();
        String accountNumber = accountDto.getAccountNumber();
        accountFacade.depositMoney(accountId, BigDecimal.TEN);
        PaymentDto paymentDto = new PaymentDto(200L, accountId, accountId, null, Currency.PLN, LocalDateTime.now(), BigDecimal.TEN);
        int pinCode = accountDto.getPinCode();

        //when
        paymentFacade.makePayment(paymentDto, pinCode);
        paymentFacade.makePayment(paymentDto, pinCode);
        List<PaymentDto> paymentDtoList = paymentFacade.getPaymentListOfAccount(accountNumber);
        boolean anyPlN = paymentDtoList.stream().anyMatch(a -> a.getCurrency().equals(Currency.PLN));
        boolean accountToId = paymentDtoList.stream().anyMatch(a -> a.getAccountToId().equals(accountId));

        //then
        Assert.assertEquals(4, paymentDtoList.size());
        Assert.assertTrue(anyPlN);
        Assert.assertTrue(accountToId);
    }

    private User getSimpleUser() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new User(generatedString, "1234", "mail@mail.com", 2000.0);
    }

}
