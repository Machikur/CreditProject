package com.bank.service;

import com.bank.domain.Payment;
import com.bank.repository.PaymentDao;
import com.bank.exception.PaymentNotFoundException;
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

    public void savePayment(Payment payment) {
        paymentDao.save(payment);
    }


    public Payment findById(Long paymentId) throws PaymentNotFoundException {
        return paymentDao.findById(paymentId).orElseThrow(PaymentNotFoundException::new);
    }

    public List<Payment> getAllAccountsPayments(Long accountId) {
        return paymentDao.getAllByAccountFrom_IdEqualsOrAccountToIdEquals(accountId, accountId);
    }

}
