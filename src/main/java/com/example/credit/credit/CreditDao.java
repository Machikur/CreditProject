package com.example.credit.credit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditDao extends CrudRepository<Credit,Long> {
}
