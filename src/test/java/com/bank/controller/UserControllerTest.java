package com.bank.controller;

import com.bank.bank.Status;
import com.bank.client.Currency;
import com.bank.dto.CreditDto;
import com.bank.dto.UserDto;
import com.bank.facade.UserFacade;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

    @Test
    public void getUserTest() throws Exception {
        //given
        when(userFacade.findUserByNameAndPassword(anyString(), anyString())).thenReturn(getSimpleUserDto());

        //when and then
        mockMvc.perform(get("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "1")
                .param("password", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcin")))
                .andExpect(jsonPath("$.credits.size()", is(1)))
                .andExpect(jsonPath("$.accounts.size()", is(0)))
                .andExpect(jsonPath("$.password", is("1234")));

        verify(userFacade, times(1)).findUserByNameAndPassword(anyString(), anyString());
    }

    @Test
    public void deleteUserTest() throws Exception {

        //when and then
        mockMvc.perform(delete("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

        verify(userFacade, times(1)).deleteUser(anyLong());
    }

    @Test
    public void updateUserTest() throws Exception {
        //given
        UserDto user = new UserDto();

        Gson gson = new Gson();
        String userToJson = gson.toJson(user);

        //when and then
        mockMvc.perform(put("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToJson)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

        verify(userFacade, times(1)).updateUser(any());
    }

    @Test
    public void saveUserTest() throws Exception {
        //given
        UserDto user = new UserDto();
        when(userFacade.saveUser(any())).thenReturn(getSimpleUserDto());

        Gson gson = new Gson();
        String userToJson = gson.toJson(user);

        //when and then
        mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToJson)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcin")))
                .andExpect(jsonPath("$.credits.size()", is(1)))
                .andExpect(jsonPath("$.accounts.size()", is(0)))
                .andExpect(jsonPath("$.password", is("1234")));

        verify(userFacade, times(1)).saveUser(any());
    }

    @Test
    public void getCurrenciesTest() throws Exception {
        //given
        when(userFacade.getListOfUsersCurrencies(anyLong())).thenReturn(Arrays.asList(Currency.values()));

        //when and then
        mockMvc.perform(get("/v1/userCurrencies")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(4)));

        verify(userFacade, times(1)).getListOfUsersCurrencies(anyLong());
    }

    public UserDto getSimpleUserDto() {
        return new UserDto(1L, "Marcin", "1234", "mail@mail.com",
                new ArrayList<>(), Collections.singletonList(new CreditDto()), 2000.0, Status.NEW, LocalDate.now());
    }
}
