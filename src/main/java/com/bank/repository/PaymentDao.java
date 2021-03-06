package com.bank.repository;

import com.bank.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDao extends CrudRepository<Payment, Long> {
}
