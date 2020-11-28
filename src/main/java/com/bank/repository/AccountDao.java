package com.bank.repository;

import com.bank.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends CrudRepository<Account, Long> {

    Optional<Account> findByAccountNumberAndPinCode(String accountNumber,int pinCode);

    Optional<Account> findByAccountNumber(String accountNumber);
}
