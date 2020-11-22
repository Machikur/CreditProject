package com.bank.mapper;

import com.bank.domain.Account;
import com.bank.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    private PaymentMapper paymentMapper;

    @Autowired
    public AccountMapper(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }


    public AccountDto mapToAccountDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.isOpen(),
                account.getCashBalance(),
                account.getUser().getId(),
                account.getCurrency(),
                account.getAccountNumber(),
                account.getPinCode(),
                account.getCreateTime(),
                paymentMapper.mapToDtoList(account.getPaymentsFrom()),
                paymentMapper.mapToDtoList(account.getPaymentsTo()));
    }

    public List<AccountDto> mapToDtoList(List<Account> accounts) {
        return accounts.stream()
                .map(this::mapToAccountDto)
                .collect(Collectors.toList());
    }

}
