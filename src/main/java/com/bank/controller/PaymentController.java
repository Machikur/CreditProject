package com.bank.controller;

import com.bank.dto.PaymentDto;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.AccountOperationException;
import com.bank.exception.CreditNotFoundException;
import com.bank.exception.PaymentCreateException;
import com.bank.facade.PaymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Autowired
    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping("/payment")
    public PaymentDto makePayment(@RequestBody PaymentDto paymentDto, @RequestParam int pinNumber) throws PaymentCreateException, AccountNotFoundException, AccountOperationException, CreditNotFoundException {
        return paymentFacade.makePayment(paymentDto, pinNumber);
    }

    @GetMapping("/userPayments")
    public List<PaymentDto> getListOfUserPayments(@RequestParam Long accountId) throws AccountNotFoundException {
        return paymentFacade.getPaymentListOfUser(accountId);
    }

    @GetMapping("/accountPayments")
    public List<PaymentDto> getListOfAccountPayments(@RequestParam Long accountId) throws AccountNotFoundException {
        return paymentFacade.getPaymentListOfAccount(accountId);
    }
}
