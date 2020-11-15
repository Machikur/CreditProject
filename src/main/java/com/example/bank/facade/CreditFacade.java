package com.example.bank.facade;

import com.example.bank.bank.CreditType;
import com.example.bank.domain.Account;
import com.example.bank.domain.Credit;
import com.example.bank.domain.CreditOptions;
import com.example.bank.domain.User;
import com.example.bank.dto.CreditDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CreditCreateException;
import com.example.bank.exception.CreditNotFoundException;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.mapper.CreditMapper;
import com.example.bank.service.AccountService;
import com.example.bank.service.CreditEngine;
import com.example.bank.service.CreditService;
import com.example.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CreditFacade {

    private final AccountService accountService;
    private final UserService userService;
    private final CreditMapper creditMapper;
    private final CreditService creditService;
    private final CreditEngine creditEngine;

    @Autowired
    public CreditFacade(AccountService accountService, UserService userService, CreditMapper creditMapper, CreditService creditService, CreditEngine creditEngine) {
        this.accountService = accountService;
        this.userService = userService;
        this.creditMapper = creditMapper;
        this.creditService = creditService;
        this.creditEngine = creditEngine;
    }

    public CreditDto createCreditForUser(Long userId, Long accountId, BigDecimal quote, CreditType creditType)
            throws UserNotFoundException, AccountNotFoundException, CreditCreateException {
        User user = userService.findById(userId);
        Account account = accountService.findAccount(accountId);
        if (creditEngine.getMaxQuoteByAccountStatus(user.getStatus()).compareTo(quote) < 0) {
            log.error("Kredyt nie został udzielony ponieważ wybrana kwota jest za wysoka");
            throw new CreditCreateException("Wybrana kwota jest za wysoka");
        }
        if (!creditEngine.checkAvailableCreditsTypeForAccount(user.getStatus()).contains(creditType)) {
            log.error("Kredyt nie został udzielony ponieważ status konta na to nie pozwala");
            throw new CreditCreateException("Nie można udzielić kredytu dla wybranego statusu");
        }
        Credit credit = new Credit();
        double interest = creditEngine.countInterest(user.getStatus(), creditType) / 100;
        credit.setAmountToPay(quote.add(quote.multiply(BigDecimal.valueOf(interest))));
        credit.setUser(user);
        credit.setCurrency(account.getCurrency());
        credit.setFinishTime(creditEngine.getFinishTime(creditType));
        user.getCredits().add(credit);
        creditService.saveCredit(credit);
        log.info("Stworzono nowy kredyt dla uzytkownika {} {} o wartości {}",
                user.getFirstName(), user.getLastName(), quote);
        return creditMapper.mapToCreditDto(credit);
    }

    public CreditDto getCredit(Long creditId) throws CreditNotFoundException {
        return creditMapper.mapToCreditDto(creditService.getCredit(creditId));
    }

    public List<CreditDto> getCreditsForUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return creditMapper.mapToListDto(user.getCredits());
    }

    public void deleteCredit(Long creditId) throws CreditNotFoundException {
        Credit credit = creditService.getCredit(creditId);
        log.info("Usunieto kredyt użytkownika {} {} o numerze id {}",
                credit.getUser().getFirstName(), credit.getUser().getLastName(), creditId);
        creditService.deleteCredit(credit);
    }

    public CreditOptions getOptionsForUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return new CreditOptions(
                creditEngine.getMaxQuoteByAccountStatus(user.getStatus()),
                creditEngine.checkAvailableCreditsTypeForAccount(user.getStatus()));
    }


}

