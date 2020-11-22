package com.bank.mapper;

import com.bank.domain.Payment;
import com.bank.dto.PaymentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    public Payment mapToPayment(PaymentDto paymentDto) {
        return new Payment(paymentDto.getCurrency(),
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
