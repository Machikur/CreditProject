package com.example.credit.mapper;

import com.example.credit.user.User;
import com.example.credit.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMailAddress(),
                user.getAccounts(),
                user.getMonthlyEarnings()
        );
    }

    public User mapToUser(UserDto userDto) {
        return new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getMailAddress(),
                userDto.getAccounts(),
                userDto.getMonthlyEarnings());
    }
}
