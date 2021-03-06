package com.bank.repository;

import com.bank.domain.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditDao extends CrudRepository<Credit, Long> {

    List<Credit> findAllByFinishTimeBeforeAndIsFinished(LocalDate date, Boolean finished);

}
