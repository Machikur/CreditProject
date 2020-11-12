package com.example.credit.service;

import com.example.credit.credit.Credit;
import com.example.credit.credit.CreditDao;
import com.example.credit.exception.CreditNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final CreditDao creditDao;

    @Autowired
    public CreditService(CreditDao creditDao) {
        this.creditDao = creditDao;
    }


    public Credit saveCredit(Credit credit){
        return creditDao.save(credit);
    }

    public Credit getCredit(Long creditId) throws CreditNotFoundException {
        return creditDao.findById(creditId).orElseThrow(CreditNotFoundException::new);
    }

    public void deleteCredit(Credit credit){
        creditDao.delete(credit);
    }

    public boolean existById(Long creditId){
        return creditDao.existsById(creditId);
    }


}
