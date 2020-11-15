package com.example.bank.facade;

import com.example.bank.domain.User;
import com.example.bank.dto.UserDto;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.mapper.UserMapper;
import com.example.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class UserFacade {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserFacade(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userService.saveUser(userMapper.mapToUser(userDto));
        log.info("Zapisano użytownika {} {}", user.getFirstName(), user.getLastName());
        return userMapper.mapToUserDto(user);
    }

    public String deleteUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        userService.deleteUser(user);
        String message = "Uzytkownik z id: " + userId + ", został usunięty";
        log.info(message);
        return message;
    }

    public UserDto getUserById(Long userId) throws UserNotFoundException {
        return userMapper.mapToUserDto(userService.findById(userId));
    }

    public UserDto updateUser(UserDto userDto) throws UserNotFoundException {
        User user = userService.findById(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMailAddress(userDto.getMailAddress());
        user.setMonthlyEarnings(userDto.getMonthlyEarnings());
        user.updateStatus();
        userService.saveUser(user);
        log.info("Zaaktualizowano użytownika {} {}", user.getFirstName(), user.getLastName());
        return userMapper.mapToUserDto(user);
    }
}
