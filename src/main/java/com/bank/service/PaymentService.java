package com.bank.service;

import com.bank.domain.Payment;
import com.bank.repository.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public void savePayment(Payment payment) {
        paymentDao.save(payment);
    }

}
