package com.bank.mapper;

import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.dto.CreditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreditMapper {


    private final PaymentMapper paymentMapper;

    @Autowired
    public CreditMapper(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    public Credit mapToCredit(CreditDto creditDto, User user) {
        return new Credit(
                user,
                creditDto.getAmountToPay(),
                creditDto.getCurrency(),
                creditDto.getFinishTime()
        );
    }

    public CreditDto mapToCreditDto(Credit credit) {
        return new CreditDto(credit.getId(),
                credit.getUser().getId(),
                credit.getAmountToPay(),
                credit.getAmountPaid(),
                credit.getCurrency(),
                credit.getFinishTime(),
                credit.getStartTime(),
                credit.isFinished(),
                paymentMapper.mapToDtoList(credit.getPaymentsFrom()));
    }

    public List<CreditDto> mapToListDto(List<Credit> credits) {
        return credits.stream()
                .map(this::mapToCreditDto)
                .collect(Collectors.toList());
    }
}
