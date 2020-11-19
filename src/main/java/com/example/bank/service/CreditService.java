package com.example.bank.service;

import com.example.bank.domain.Credit;
import com.example.bank.exception.CreditNotFoundException;
import com.example.bank.repository.CreditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreditService {

    private final CreditDao creditDao;

    @Autowired
    public CreditService(CreditDao creditDao) {
        this.creditDao = creditDao;
    }


    public void saveCredit(Credit credit) {
        creditDao.save(credit);
    }

    public Credit getCredit(Long creditId) throws CreditNotFoundException {
        return creditDao.findById(creditId).orElseThrow(CreditNotFoundException::new);
    }

    public void deleteCredit(Credit credit) {
        creditDao.delete(credit);
    }


    public List<Credit> findAllByFinishTimeBeforeNow() {
        return creditDao.findAllByFinishTimeBefore(LocalDate.now());
    }


}
