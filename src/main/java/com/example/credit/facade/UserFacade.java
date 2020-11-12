package com.example.credit.facade;

import com.example.credit.exception.UserNotFoundException;
import com.example.credit.mapper.UserMapper;
import com.example.credit.service.UserService;
import com.example.credit.user.User;
import com.example.credit.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserDto saveUser(UserDto userDto) {
        User user = userService.saveUser(userMapper.mapToUser(userDto));
        log.info("Zapisano użytownika " + user.getFirstName(), user.getLastName());
        return userMapper.mapToUserDto(user);
    }

    public String deleteUser(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        userService.deleteUser(user);
        String message = "Uzytkownik z id:" + userId + ", został usunięty";
        log.info(message);
        return message;
    }

    public UserDto getUserById(Long userId) throws UserNotFoundException {
        return userMapper.mapToUserDto(userService.findById(userId));
    }

    public UserDto updateUser(UserDto userDto) throws UserNotFoundException {
        if (!userService.existById(userDto.getId())) {
            throw new UserNotFoundException();
        }
        User user = userMapper.mapToUser(userDto);
        user.setId(userDto.getId());
        userService.saveUser(user);
        log.info("Uzytkownik z id:" + userDto.getId() + ", został zaktualizowany");
        return userMapper.mapToUserDto(user);
    }
}
