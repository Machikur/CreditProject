package com.example.bank.controller;

import com.example.bank.dto.UserDto;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.exception.UserOperationException;
import com.example.bank.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/user")
    public UserDto getUser(@RequestParam Long userId) throws UserNotFoundException {
        return userFacade.getUserById(userId);
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
