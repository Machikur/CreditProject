package com.bank.controller;

import com.bank.client.Currency;
import com.bank.dto.AccountDto;
import com.bank.facade.AccountFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountFacade accountFacade;

    @Test
    public void controllerPostTest() throws Exception {
        //given
        AccountDto accountDto = getSimpleAccountDto();
        when(accountFacade.createNewAccount(any(), any())).thenReturn(accountDto);

        //when && then
        mockMvc.perform(post("/v1/account")
                .param("userId", "1")
                .param("currency", "EUR")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.open", is(true)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$.pinCode", is(2222)));

    }

    @Test
    public void controllerGetTest() throws Exception {
        //given
        AccountDto accountDto = getSimpleAccountDto();
        when(accountFacade.getAccount(any())).thenReturn(accountDto);

        //when && then

        mockMvc.perform(get("/v1/account")
                .param("accountId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.open", is(true)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$.pinCode", is(2222)));
    }

    @Test
    public void controllerGetAccountsTest() throws Exception {
        //given
        List<AccountDto> accountsDto = Arrays.asList(getSimpleAccountDto(), getSimpleAccountDto(), getSimpleAccountDto());
        when(accountFacade.getAccountsOfUser(any())).thenReturn(accountsDto);

        //when && then

        mockMvc.perform(get("/v1/accounts")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].open", is(true)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$[0].pinCode", is(2222)));
    }

    @Test
    public void depositTest() throws Exception {
        //given
        AccountDto accountDto = getSimpleAccountDto();
        when(accountFacade.depositMoney(any(), any())).thenReturn(accountDto);

        //when && then
        mockMvc.perform(put("/v1/deposit")
                .param("accountId", "1")
                .param("quote", "200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.open", is(true)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$.pinCode", is(2222)));
    }

    private AccountDto getSimpleAccountDto() {
        return new AccountDto(1L, true, BigDecimal.ZERO, 1L, Currency.EUR, "22 2222 2222 2222 2222 2222 2222",
                2222, LocalDate.now(), new ArrayList<>(), new ArrayList<>());
    }
}