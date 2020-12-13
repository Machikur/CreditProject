package com.bank.service;

import com.bank.domain.Credit;
import com.bank.exception.CreditNotFoundException;
import com.bank.repository.CreditDao;
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

    public Credit findCredit(Long creditId) throws CreditNotFoundException {
        return creditDao.findById(creditId).orElseThrow(CreditNotFoundException::new);
    }

    public void deleteCredit(Credit credit) {
        creditDao.delete(credit);
    }

    public List<Credit> findAllByFinishTimeBeforeAndIsFinished(LocalDate dateToCompare, boolean isFinished) {
        return creditDao.findAllByFinishTimeBeforeAndIsFinished(dateToCompare, isFinished);
    }

}
