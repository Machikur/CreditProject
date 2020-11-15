package com.example.bank.service;

import com.example.bank.domain.User;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public User findById(Long userId) throws UserNotFoundException {
        return userDao.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public boolean existById(Long id) {
        return userDao.existsById(id);
    }

}
