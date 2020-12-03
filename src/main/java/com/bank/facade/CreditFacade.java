package com.bank.facade;

import com.bank.bank.CreditEngine;
import com.bank.bank.CreditType;
import com.bank.domain.Account;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.dto.CreditDto;
import com.bank.dto.CreditOptionsDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.CreditCreateException;
import com.bank.exception.CreditNotFoundException;
import com.bank.exception.UserNotFoundException;
import com.bank.mapper.CreditMapper;
import com.bank.service.AccountService;
import com.bank.service.CreditService;
import com.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public void createCreditForUser(Long userId, Long accountId, BigDecimal quote, CreditType creditType)
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
        double interest = creditEngine.countInterest(user.getStatus(), creditType) / 100;
        BigDecimal amountToPay =quote.add(quote.multiply(BigDecimal.valueOf(interest)));
        Credit credit = new Credit(user,amountToPay,account.getCurrency(),creditEngine.getFinishTime(creditType));
        user.getCredits().add(credit);
        account.depositMoney(quote);
        userService.saveUser(user);
        log.info("Stworzono nowy kredyt dla uzytkownika {} o wartości {}",
                user.getName(), quote);
    }

    public List<CreditDto> getCreditsForUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return creditMapper.mapToListDto(user.getCredits());
    }

    public void deleteCredit(Long creditId) throws CreditNotFoundException {
        Credit credit = creditService.getCredit(creditId);
        if (credit.isFinished()) {
            creditService.deleteCredit(credit);
            log.info("Usunieto kredyt użytkownika {}  o numerze id {}",
                    credit.getUser().getName(), creditId);
        }
    }

    public double countInterest(Long userId, int days) throws UserNotFoundException {
        User user = userService.findById(userId);
        return creditEngine.countInterest(user.getStatus(), CreditType.findByKey(days));
    }

    public CreditOptionsDto getOptionsForUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return new CreditOptionsDto(
                creditEngine.getMaxQuoteByAccountStatus(user.getStatus()),
                creditEngine.checkAvailableCreditsTypeForAccount(user.getStatus()));
    }

}

