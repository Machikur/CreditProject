package com.bank.repository;

import com.bank.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDao extends CrudRepository<Payment, Long> {
}
