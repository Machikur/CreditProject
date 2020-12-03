package com.bank.mapper;

import com.bank.bank.Status;
import com.bank.domain.User;
import com.bank.dto.UserDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void mapToDtoTest() {
        //given
        User user = new User("Michał", "1234", "mail@mail.com", 2000.0);

        //when
        UserDto userDto = userMapper.mapToUserDto(user);

        //then
        Assert.assertEquals(user.getId(), userDto.getId());
        Assert.assertEquals(user.getPassword(), userDto.getPassword());
        Assert.assertEquals(user.getMailAddress(), userDto.getMailAddress());
        Assert.assertEquals(user.getMonthlyEarnings(), userDto.getMonthlyEarnings());
    }

    @Test
    public void mapToUserTest() {
        //given
        UserDto userDto = new UserDto(1L, "Michał", "1234", "mail@mail.com"
                , new ArrayList<>(), new ArrayList<>(), 2000.0, Status.STANDARD, LocalDate.now());

        //when
        User user = userMapper.mapToUser(userDto);

        //then
        Assert.assertEquals(user.getName(), userDto.getName());
        Assert.assertEquals(user.getPassword(), userDto.getPassword());
        Assert.assertEquals(user.getMailAddress(), userDto.getMailAddress());
        Assert.assertEquals(user.getMonthlyEarnings(), userDto.getMonthlyEarnings());
    }

}