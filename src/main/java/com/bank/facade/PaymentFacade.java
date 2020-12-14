package com.bank.facade;

import com.bank.domain.Account;
import com.bank.domain.Payment;
import com.bank.domain.PaymentDirection;
import com.bank.domain.User;
import com.bank.dto.PaymentDto;
import com.bank.exception.*;
import com.bank.mapper.PaymentMapper;
import com.bank.service.AccountService;
import com.bank.service.CurrencyService;
import com.bank.service.PaymentService;
import com.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PaymentFacade {

    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;
    private final AccountService accountService;
    private final UserService userService;
    private final CurrencyService currencyService;

    @Autowired
    public PaymentFacade(PaymentMapper paymentMapper, PaymentService paymentService, AccountService accountService, UserService userService, CurrencyService currencyService) {
        this.paymentMapper = paymentMapper;
        this.paymentService = paymentService;
        this.accountService = accountService;
        this.userService = userService;
        this.currencyService = currencyService;
    }

    public PaymentDto makePayment(PaymentDto paymentDto, int pinNumber) throws AccountNotFoundException, PaymentCreateException, CreditNotFoundException, AccountOperationException {
        Account accountFrom = accountService.findAccount(paymentDto.getAccountFromId());
        paymentDto.setCurrency(accountFrom.getCurrency());
        if (accountFrom.getPinCode() != pinNumber) {
            throw new PaymentCreateException("Podano błędny kod pin");
        }
        Payment payment = pay(paymentMapper.mapToPayment(paymentDto));
        paymentService.savePayment(payment);
        return paymentMapper.mapToPaymentDto(payment);
    }

    public List<PaymentDto> getPaymentListOfUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        List<Account> list = user.getAccounts();
        return list.stream()
                .flatMap(s -> s.getPaymentsFrom().stream())
                .map(paymentMapper::mapToPaymentDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentListOfAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountService.findAccountByAccountNumber(accountNumber);
        List<Payment> list = account.getPaymentsFrom();
        list.addAll(account.getPaymentsTo());
        return paymentMapper.mapToDtoList(list);
    }

    private Payment pay(Payment payment) throws AccountOperationException {
        Account from = payment.getAccountFrom();
        BigDecimal quote = payment.getQuote();
        from.withdrawMoney(quote);
        PaymentDirection to;
        if (payment.getAccountTo() != null) {
            to = payment.getAccountTo();
        } else {
            to = payment.getCredit();
        }
        BigDecimal quoteAfterExchange = currencyService.findExchangeQuote(from.getCurrency(), to.getCurrency(), quote);
        to.depositMoney(quoteAfterExchange);
        log.info("Wykonano płatnośc z konta id: {} do odbiorcy z id: {}, o wartości {} {}", from.getId(),
                to.getId(), quote, from.getCurrency());
        return payment;
    }

}
