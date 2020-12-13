package com.bank.mapper;

import com.bank.client.currency.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldReturnAccountDto() {
        //given
        User user = new User();
        user.setId(1L);
        Account account = new Account(user, Currency.EUR);
        account.setPinCode(1234);
        account.setCreateTime(LocalDate.now());
        account.setId(1L);
        account.setAccountNumber("1234");
        account.setPinCode(1234);

        //when
        AccountDto result = accountMapper.mapToAccountDto(account);

        //then
        Assert.assertEquals(result.getAccountNumber(), account.getAccountNumber());
        Assert.assertEquals(result.getCashBalance(), account.getCashBalance());
        Assert.assertEquals(result.getId(), account.getId());
        Assert.assertEquals(result.getCreateTime(), account.getCreateTime());
        Assert.assertEquals(result.getCurrency(), account.getCurrency());
        Assert.assertEquals(result.getPaymentsFrom().size(), account.getPaymentsFrom().size());
        Assert.assertEquals(result.getPinCode(), account.getPinCode());
    }

    @Test
    public void shouldReturnListOfAccountDto() {
        //given
        User user = new User();
        user.setId(1L);
        Account account = new Account(user, Currency.EUR);

        List<Account> accounts = Arrays.asList(account, account, account, account);

        //when
        List<AccountDto> result = accountMapper.mapToDtoList(accounts);

        //then
        Assert.assertEquals(result.size(), accounts.size());
    }

}