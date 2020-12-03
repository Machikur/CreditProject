package com.bank.facade;

import com.bank.client.Currency;
import com.bank.domain.Account;
import com.bank.domain.User;
import com.bank.dto.AccountDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.mapper.AccountMapper;
import com.bank.service.AccountService;
import com.bank.service.UserService;
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

    private final AccountService accountService;
    private final UserService userService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountFacade(AccountService accountService, UserService userService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.userService = userService;
        this.accountMapper = accountMapper;
    }

    public AccountDto createNewAccount(Long userId, Currency currency) throws UserNotFoundException {
        User user = userService.findById(userId);
        Account account = new Account(user, currency);
        account.setAccountNumber(accountNumberCreator());
        account.setPinCode(pinCreator());
        user.getAccounts().add(account);
        accountService.saveAccount(account);
        log.info("Konto o id: {}, dla użytkownika {}   zostało utworzone",
                account.getId(), user.getName());
        return accountMapper.mapToAccountDto(account);
    }

    public List<AccountDto> getAccountsOfUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return accountMapper.mapToDtoList(user.getAccounts());
    }

    public void depositMoney(Long accountId, BigDecimal quote) throws AccountNotFoundException {
            Account account = accountService.findAccount(accountId);
            account.depositMoney(quote);
    }

    public Double getAllCashInCurrency(Long userId, Currency currency) throws UserNotFoundException {
        List<AccountDto> accounts = getAccountsOfUser(userId);
        return accounts.stream()
                .filter(a -> a.getCurrency().equals(currency))
                .mapToDouble(a -> a.getCashBalance().doubleValue())
                .sum();

    }

    public void deleteAccount(Long accountId, int pinNumber) throws AccountNotFoundException {
        Account account = accountService.findAccount(accountId);
        if (account.getPinCode() == pinNumber && account.getCashBalance().compareTo(BigDecimal.ZERO) == 0) {
            accountService.deleteAccount(account);
            log.info("Konto o id: {} zostało usunięte",
                    account.getId());
        }

    }

    public void withdrawal(Long accountId, BigDecimal quote) throws AccountOperationException, AccountNotFoundException {
        Account account = accountService.findAccount(accountId);
        account.withdrawMoney(quote);
        accountService.saveAccount(account);
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
