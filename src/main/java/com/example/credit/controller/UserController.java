package com.example.credit.controller;

import com.example.credit.exception.UserNotFoundException;
import com.example.credit.facade.UserFacade;
import com.example.credit.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("/user")
    public UserDto getUser(@RequestParam Long userId) throws UserNotFoundException {
        return userFacade.getUserById(userId);
    }

    @PostMapping("/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return userFacade.saveUser(userDto);
    }

    @DeleteMapping("/user")
    public String deleteUser(@RequestParam Long userId) throws UserNotFoundException {
        return userFacade.deleteUser(userId);
    }

    @PutMapping("/user")
    public UserDto updateUser(@RequestBody UserDto userDto) throws UserNotFoundException {
        return userFacade.updateUser(userDto);
    }
}
