package com.example.bank.repository;

import com.example.bank.domain.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditDao extends CrudRepository<Credit, Long> {

    List<Credit> findAllByFinishTimeBefore(LocalDate date);
}
