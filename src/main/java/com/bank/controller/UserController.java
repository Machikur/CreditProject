package com.bank.controller;

import com.bank.client.Currency;
import com.bank.dto.UserDto;
import com.bank.exception.UserNotFoundException;
import com.bank.exception.UserOperationException;
import com.bank.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/user")
    public UserDto getUser(@RequestParam String name, @RequestParam String password) throws UserNotFoundException {
        return userFacade.findUserByNameAndPassword(name, password);
    }

    @GetMapping("/userCurrencies")
    public Collection<Currency> getListOfCurrenciesForUser(@RequestParam Long userId) throws UserNotFoundException {
        return userFacade.getListOfUsersCurrencies(userId);
    }

    @PostMapping("/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return userFacade.saveUser(userDto);
    }

    @DeleteMapping("/user")
    public String deleteUser(@RequestParam Long userId) throws UserNotFoundException, UserOperationException {
        return userFacade.deleteUser(userId);
    }

    @PutMapping("/user")
    public UserDto updateUser(@RequestBody UserDto userDto) throws UserNotFoundException {
        return userFacade.updateUser(userDto);
    }

}
