package com.bank.facade;

import com.bank.client.Currency;
import com.bank.domain.User;
import com.bank.dto.UserDto;
import com.bank.exception.UserNotFoundException;
import com.bank.exception.UserOperationException;
import com.bank.mapper.UserMapper;
import com.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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
        log.info("Zapisano użytownika {} ", user.getName());
        return userMapper.mapToUserDto(user);
    }

    public String deleteUser(Long userId) throws UserNotFoundException, UserOperationException {
        User user = userService.findById(userId);
        if (checkIfCreditsAndAccountsAreDone(user)) {
            throw new UserOperationException("Użytkownik ma niespłacone kredyty lub pozostały mu pieniądze na koncie");
        }
        userService.deleteUser(user);
        String message = "Uzytkownik z id: " + userId + ", został usunięty";
        log.info(message);
        return message;
    }


    public UserDto findUserByNameAndPassword(String name, String password) throws UserNotFoundException {
        return userMapper.mapToUserDto(userService.findByNameAndPassword(name, password));
    }

    public UserDto updateUser(UserDto userDto) throws UserNotFoundException {
        User user = userService.findById(userDto.getId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setMailAddress(userDto.getMailAddress());
        user.setMonthlyEarnings(userDto.getMonthlyEarnings());
        user.updateStatus();
        userService.saveUser(user);
        log.info("Zaaktualizowano użytownika {} ", user.getName());
        return userMapper.mapToUserDto(user);
    }

    public double getAllCashBalance(Long userId, Currency currency) throws UserNotFoundException {
        User user = userService.findById(userId);
        return user.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(currency))
                .mapToDouble(a -> a.getCashBalance().doubleValue())
                .sum();
    }


    private boolean checkIfCreditsAndAccountsAreDone(User user) {
        boolean clear = true;
        if (!user.getCredits().isEmpty()) {
            clear = user.getCredits().stream()
                    .noneMatch(c -> !c.isFinished());
        }
        if (!user.getAccounts().isEmpty()) {
            clear = user.getAccounts().stream()
                    .noneMatch(a -> !a.getCashBalance().equals(BigDecimal.ZERO));
        }
        return clear;
    }
}
