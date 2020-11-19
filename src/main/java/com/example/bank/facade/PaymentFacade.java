package com.example.bank.facade;

import com.example.bank.domain.Account;
import com.example.bank.domain.Credit;
import com.example.bank.domain.Payment;
import com.example.bank.dto.PaymentDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.AccountOperationException;
import com.example.bank.exception.CreditNotFoundException;
import com.example.bank.exception.PaymentCreateException;
import com.example.bank.mapper.PaymentMapper;
import com.example.bank.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PaymentFacade {

    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;
    private final AccountService accountService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final CreditService creditService;

    @Autowired
    public PaymentFacade(PaymentMapper paymentMapper, PaymentService paymentService, AccountService accountService, UserService userService, CurrencyService currencyService, CreditService creditService) {
        this.paymentMapper = paymentMapper;
        this.paymentService = paymentService;
        this.accountService = accountService;
        this.userService = userService;
        this.currencyService = currencyService;
        this.creditService = creditService;
    }

    public PaymentDto makePayment(PaymentDto paymentDto, int pinNumber) throws AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        Account accountFrom = accountService.findAccount(paymentDto.getAccountFromId());
        if (accountFrom.getPinCode() != pinNumber) {
            throw new PaymentCreateException("Podano błędny kod pin");
        }
        Payment payment = paymentMapper.mapToPayment(paymentDto);
        payment.setAccountFrom(accountFrom);
        payment.setCurrency(accountFrom.getCurrency());
        if (paymentDto.getCreditId() != null) {
            Credit credit = creditService.getCredit(paymentDto.getCreditId());
            payToCredit(accountFrom, credit, paymentDto.getQuote());
            credit.getPaymentsFrom().add(payment);
            payment.setCredit(credit);
            creditService.saveCredit(credit);
            log.info("Przelano kwote {} {} na credyt o numerze id: {}", paymentDto.getQuote(), accountFrom.getCurrency(), credit.getId());
        } else if (paymentDto.getAccountToId() != null) {
            Account accountTo = accountService.findAccount(paymentDto.getAccountToId());
            payToAccount(accountFrom, accountTo, paymentDto.getQuote());
            payment.setAccountTo(accountTo);
            accountService.saveAccount(accountTo);
            log.info("Przelano kwote {}{} na konto o numerze id: {}", paymentDto.getQuote(), accountFrom.getCurrency(), accountTo.getId());
        }
        log.info("Transakcja zakończona pomyślnie");
        accountFrom.getPaymentsFrom().add(payment);
        accountService.saveAccount(accountFrom);
        return paymentMapper.mapToPaymentDto(payment);
    }

    public List<PaymentDto> getPaymentListOfUser(Long userId) throws AccountNotFoundException {
        Account account = accountService.findAccount(userId);
        List<Payment> list = account.getPaymentsFrom();
        list.addAll(account.getPaymentsTo());
        return paymentMapper.mapToDtoList(list);
    }

    public List<PaymentDto> getPaymentListOfAccount(Long accountId) throws AccountNotFoundException {
        if (!accountService.existById(accountId)) {
            throw new AccountNotFoundException();
        }
        List<Payment> list = paymentService.getAllAccountsPayments(accountId);
        return paymentMapper.mapToDtoList(list);
    }


    private void payToAccount(Account from, Account to, BigDecimal quote) throws AccountOperationException {
        from.withdrawMoney(quote);
        BigDecimal quoteAfterExchange = currencyService.getExchangeQuote(from.getCurrency(), to.getCurrency(), quote);
        to.depositMoney(quoteAfterExchange);
        accountService.saveAccount(from);
        accountService.saveAccount(to);
    }

    private void payToCredit(Account from, Credit to, BigDecimal quote) throws AccountOperationException {
        BigDecimal quoteAfterExchange = currencyService.getExchangeQuote(from.getCurrency(), to.getCurrency(), quote);
        from.withdrawMoney(quote);
        to.makePayment(quoteAfterExchange);
        accountService.saveAccount(from);
        creditService.saveCredit(to);
    }
}
