package com.bank.controller;

import com.bank.aop.ToValidate;
import com.bank.dto.PaymentDto;
import com.bank.exception.*;
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

    @ToValidate
    @PostMapping("/payment")
    public PaymentDto makePayment(@RequestParam Long userId, @RequestBody PaymentDto paymentDto, @RequestParam int pinNumber) throws PaymentCreateException, AccountNotFoundException, AccountOperationException, CreditNotFoundException {
        return paymentFacade.makePayment(paymentDto, pinNumber);
    }

    @ToValidate
    @GetMapping("/userPayments")
    public List<PaymentDto> getListOfUserPayments(@RequestParam Long userId) throws UserNotFoundException {
        return paymentFacade.getPaymentListOfUser(userId);
    }

    @GetMapping("/accountPayments")
    public List<PaymentDto> getListOfAccountPayments(@RequestParam String accountNumber) throws AccountNotFoundException {
        return paymentFacade.getPaymentListOfAccount(accountNumber);
    }

}
