package com.example.bank.controller;

import com.example.bank.dto.PaymentDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.AccountOperationException;
import com.example.bank.exception.CreditNotFoundException;
import com.example.bank.exception.PaymentCreateException;
import com.example.bank.facade.PaymentFacade;
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
    public PaymentDto makePayment(@RequestBody PaymentDto paymentDto, int pinNumber) throws PaymentCreateException, AccountNotFoundException, AccountOperationException, CreditNotFoundException {
        return paymentFacade.makePayment(paymentDto, pinNumber);
    }

    @GetMapping("/userPayments")
    public List<PaymentDto> getListOfUserPayments(@RequestParam Long accountId) {
       return null;
    }

    @GetMapping("/accountPayments")
    public List<PaymentDto> getListOfAccountPayments(@RequestParam Long accountId) throws AccountNotFoundException {
        return paymentFacade.getPaymentListOfUser(accountId);
    }
}
