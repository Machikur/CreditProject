package com.example.bank.controller;

import com.example.bank.bank.CreditType;
import com.example.bank.dto.CreditDto;
import com.example.bank.dto.CreditOptionsDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CreditCreateException;
import com.example.bank.exception.CreditNotFoundException;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.facade.CreditFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CreditController {

    private final CreditFacade creditFacade;

    @Autowired
    public CreditController(CreditFacade creditFacade) {
        this.creditFacade = creditFacade;
    }

    @PostMapping("/credit")
    public CreditDto createCredit(@RequestParam Long userId, @RequestParam Long accountId,
                                  @RequestParam BigDecimal quote, @RequestParam Integer days)
            throws UserNotFoundException, AccountNotFoundException, CreditCreateException {
        return creditFacade.createCreditForUser(userId, accountId, quote, CreditType.findByKey(days));
    }

    @GetMapping("/credit")
    public CreditDto getCreditDetails(@RequestParam Long creditId) throws CreditNotFoundException {
        return creditFacade.getCredit(creditId);
    }

    @GetMapping("/credits")
    public List<CreditDto> getCreditsByUser(@RequestParam Long userId) throws UserNotFoundException {
        return creditFacade.getCreditsForUser(userId);
    }

    @DeleteMapping("/credit")
    public void deleteCredit(@RequestParam Long creditId) throws CreditNotFoundException {
        creditFacade.deleteCredit(creditId);
    }

    @GetMapping("/userOptions")
    public CreditOptionsDto getOptionsForUser(@RequestParam Long userId) throws UserNotFoundException {
        return creditFacade.getOptionsForUser(userId);
    }


}
