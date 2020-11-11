package com.example.credit.mapper;

import com.example.credit.customer.User;
import com.example.credit.customer.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMail(),
                user.getAccounts(),
                user.getMonthlyEarnings()
        );
    }

    public User mapToUser(UserDto userDto) {
        return new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getMail(),
                userDto.getAccounts(),
                userDto.getMonthlyEarnings());
    }
}
