package com.bank.repository;

import com.bank.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

    Optional<User> findByNameAndPassword(String name,String password);

}
