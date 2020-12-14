package com.bank.facade;

import com.bank.client.currency.Currency;
import com.bank.domain.Account;
import com.bank.domain.Credit;
import com.bank.domain.User;
import com.bank.dto.UserDto;
import com.bank.exception.OperationException;
import com.bank.exception.UserNotFoundException;
import com.bank.mapper.UserMapper;
import com.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

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

    public void deleteUser(Long userId) throws UserNotFoundException, OperationException {
        User user = userService.findById(userId);
        if (!user.getCredits().stream().allMatch(Credit::isFinished) ||
                (user.getAccounts().stream().anyMatch(a -> a.getCashBalance().compareTo(BigDecimal.ZERO) != 0))) {
            throw new OperationException("Nie można usunąć użytkownika przed spłaceniem kredytów i wyczyszczeniem kont");
        }
        userService.deleteUser(user);
        log.info("Uzytkownik {} został usunięty", user.getName());
    }


    public UserDto findUserByNameAndPassword(String name, String password) throws UserNotFoundException {
        return userMapper.mapToUserDto(userService.findByNameAndPassword(name, password));
    }

    public void updateUser(UserDto userDto) throws UserNotFoundException {
        User user = userService.findById(userDto.getId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setMailAddress(userDto.getMailAddress());
        user.setMonthlyEarnings(userDto.getMonthlyEarnings());
        userService.saveUser(user);
        log.info("Zaaktualizowano użytownika {} ", user.getName());
    }

    public Collection<Currency> getListOfUsersCurrencies(Long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return user.getAccounts().stream()
                .map(Account::getCurrency)
                .collect(Collectors.toSet());
    }

}
