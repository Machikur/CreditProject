package com.example.credit.service;

import com.example.credit.customer.User;
import com.example.credit.customer.UserDao;
import com.example.credit.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public User findById(Long userId) throws UserNotFoundException {
        return userDao.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public boolean existById(Long id){
        return userDao.existsById(id);
    }

}
