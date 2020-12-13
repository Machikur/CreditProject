package com.bank.mapper;

import com.bank.domain.Account;
import com.bank.domain.Credit;
import com.bank.domain.Payment;
import com.bank.dto.PaymentDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CreditNotFoundException;
import com.bank.service.AccountService;
import com.bank.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    private final AccountService accountService;
    private final CreditService creditService;

    @Autowired
    public PaymentMapper(AccountService accountService, CreditService creditService) {
        this.accountService = accountService;
        this.creditService = creditService;
    }

    public Payment mapToPayment(PaymentDto paymentDto) throws AccountNotFoundException, CreditNotFoundException {
        Credit credit = null;
        Account accountTo = null;
        if (Objects.nonNull(paymentDto.getCreditId())) {
            credit = creditService.findCredit(paymentDto.getCreditId());
        }
        if (Objects.nonNull(paymentDto.getAccountToId())) {
            accountTo = accountService.findAccount(paymentDto.getAccountToId());
        }
        return new Payment(
                accountService.findAccount(paymentDto.getAccountFromId()),
                accountTo,
                credit,
                paymentDto.getCurrency(),
                paymentDto.getQuote());
    }

    public PaymentDto mapToPaymentDto(Payment payment) {
        Long creditId = null;
        Long accountId = null;
        if (payment.getAccountTo() != null) {
            accountId = payment.getAccountTo().getId();
        }
        if (payment.getCredit() != null) {
            creditId = payment.getCredit().getId();
        }
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getAccountFrom().getId(),
                accountId,
                creditId,
                payment.getCurrency(),
                payment.getCreateTime(),
                payment.getQuote());
    }

    public List<PaymentDto> mapToDtoList(List<Payment> payments) {
        return payments.stream()
                .map(this::mapToPaymentDto)
                .collect(Collectors.toList());

    }

}
