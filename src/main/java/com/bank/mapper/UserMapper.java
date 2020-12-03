package com.bank.mapper;

import com.bank.domain.User;
import com.bank.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AccountMapper accountMapper;

    private final CreditMapper creditMapper;

    @Autowired
    public UserMapper(AccountMapper accountMapper, CreditMapper creditMapper) {
        this.accountMapper = accountMapper;
        this.creditMapper = creditMapper;
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getMailAddress(),
                accountMapper.mapToDtoList(user.getAccounts()),
                creditMapper.mapToListDto(user.getCredits()),
                user.getMonthlyEarnings(),
                user.getStatus(),
                user.getCreateDate()
        );
    }

    public User mapToUser(UserDto userDto) {
        return new User(userDto.getName(),
                userDto.getPassword(),
                userDto.getMailAddress(),
                userDto.getMonthlyEarnings());
    }

}
