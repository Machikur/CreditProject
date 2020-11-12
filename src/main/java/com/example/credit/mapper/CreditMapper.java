package com.example.credit.mapper;

import com.example.credit.credit.Credit;
import com.example.credit.credit.CreditDto;
import com.example.credit.user.User;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    public Credit mapToCredit(CreditDto creditDto, User user) {
        return new Credit(
                user,
                creditDto.getAmountToPay(),
                creditDto.getCurrency(),
                creditDto.getFinishDate()
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
                credit.isFinished());
    }
}
