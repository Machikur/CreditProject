package com.example.bank.repository;

import com.example.bank.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends CrudRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);
}
