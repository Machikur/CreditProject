package com.example.credit.mapper;

import com.example.credit.bank.Account;
import com.example.credit.bank.AccountDto;
import com.example.credit.user.User;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account mapToAccount(AccountDto accountDto, User user){
        return new Account(
                user,
                accountDto.getCurrency(),
                accountDto.getAccountNumber());
    }

    public AccountDto mapToAccountDto(Account account){
        return new AccountDto(
                account.getId(),
                account.isOpen(),
                account.getCashBalance(),
                account.getUser().getId(),
                account.getCurrency(),
                account.getAccountNumber(),
                account.getStatus(),
                account.getCreateTime());
    }

}
