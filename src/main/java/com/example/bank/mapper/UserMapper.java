package com.example.bank.mapper;

import com.example.bank.domain.User;
import com.example.bank.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AccountMapper accountMapper;
    private final CreditMapper creditMapper;

    public UserMapper(AccountMapper accountMapper, CreditMapper creditMapper) {
        this.accountMapper = accountMapper;
        this.creditMapper = creditMapper;
    }


    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMailAddress(),
                accountMapper.mapToDtoList(user.getAccounts()),
                creditMapper.mapToListDto(user.getCredits()),
                user.getMonthlyEarnings(),
                user.getStatus(),
                user.getCreateDate()
        );
    }

    public User mapToUser(UserDto userDto) {
        return new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getMailAddress(),
                userDto.getMonthlyEarnings());
    }
}
