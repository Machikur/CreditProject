package com.example.bank.service;

import com.example.bank.domain.Payment;
import com.example.bank.exception.PaymentNotFoundException;
import com.example.bank.repository.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public Payment savePayment(Payment payment) {
        return paymentDao.save(payment);
    }

    public Payment findById(Long paymentId) throws PaymentNotFoundException {
        return paymentDao.findById(paymentId).orElseThrow(PaymentNotFoundException::new);
    }

    public List<Payment> getAllPaymentsFromAccount(Long accountId) {
        return paymentDao.getAllByAccountFromEquals(accountId);
    }

}
