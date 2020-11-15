package com.example.bank.facade;

import com.example.bank.client.Currency;
import com.example.bank.domain.Account;
import com.example.bank.domain.User;
import com.example.bank.dto.AccountDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.service.AccountService;
import com.example.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Transactional
public class AccountFacade {

    private AccountService accountService;
    private UserService userService;
    private AccountMapper accountMapper;

    @Autowired
    public AccountFacade(AccountService accountService, UserService userService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.userService = userService;
        this.accountMapper = accountMapper;
    }

    public AccountDto createNewAccount(Long userId, Currency currency) throws UserNotFoundException {
        User user = userService.findById(userId);
        Account account = accountMapper.mapToAccount(currency, user);
        account.setAccountNumber(accountNumberCreator());
        account.setPinCode(pinCreator());
        user.getAccounts().add(account);
        accountService.saveAccount(account);
        log.info("Konto o id: {}, dla użytkownika {} {}  zostało utworzone",
                account.getId(), user.getFirstName(), user.getLastName());
        return accountMapper.mapToAccountDto(account);
    }

    public AccountDto getAccount(Long accountId) throws AccountNotFoundException {
        return accountMapper.mapToAccountDto(accountService.findAccount(accountId));
    }

    public List<AccountDto> getAccountsOfUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return accountMapper.mapToDtoList(user.getAccounts());
    }

    public AccountDto depositMoney(Long accountId, BigDecimal quote) throws AccountNotFoundException {
        Account account = accountService.findAccount(accountId);
        account.depositMoney(quote);
        accountService.saveAccount(account);
        return accountMapper.mapToAccountDto(account);
    }

    private String accountNumberCreator() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            stringBuilder.append(random.nextInt(9));
            if (((i + 3) % 4 == 0 || i == 1) && i != 25) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    private int pinCreator() {
        Random random = new Random();
        return random.nextInt(8999) + 1000;
    }
}
