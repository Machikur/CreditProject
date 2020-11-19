package com.example.bank.repository;

import com.example.bank.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDao extends CrudRepository<Payment, Long> {

    List<Payment> getAllByAccountFrom_IdEqualsOrAccountToIdEquals(Long accountFromId, Long accountToId);
}
