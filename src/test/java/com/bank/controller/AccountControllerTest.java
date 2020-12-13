package com.bank.controller;

import com.bank.client.currency.Currency;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountFacade accountFacade;

    @Test
    public void createAccountTest() throws Exception {

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
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$.pinCode", is(2222)));
    }

    @Test
    public void getCurrencyCashTest() throws Exception {
        when(accountFacade.getAllCashInCurrency(any(), any())).thenReturn(1500.0);

        //when && then
        mockMvc.perform(get("/v1/account/currencyCash")
                .param("userId", "1")
                .param("currency", "PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.0"));
        verify(accountFacade, times(1)).getAllCashInCurrency(any(), any());
    }

    @Test
    public void getAccountsTest() throws Exception {
        //given
        List<AccountDto> accountsDto = Arrays.asList(getSimpleAccountDto(), getSimpleAccountDto(), getSimpleAccountDto());
        when(accountFacade.getAccountsOfUser(any())).thenReturn(accountsDto);

        //when && then
        mockMvc.perform(get("/v1/accounts")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].accountNumber", is("22 2222 2222 2222 2222 2222 2222")))
                .andExpect(jsonPath("$[0].pinCode", is(2222)));
    }

    @Test
    public void depositTest() throws Exception {

        //when && then
        mockMvc.perform(put("/v1/account/deposit")
                .param("accountId", "1")
                .param("quote", "200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(accountFacade, times(1)).depositMoney(any(), any());
    }

    @Test
    public void withdrawalTest() throws Exception {

        //when && then
        mockMvc.perform(put("/v1/account/withdrawal")
                .param("accountId", "1")
                .param("quote", "200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(accountFacade, times(1)).withdrawal(any(), any());
    }

    @Test
    public void controllerDeleteTest() throws Exception {

        //when && then
        mockMvc.perform(delete("/v1/account")
                .param("accountId", "1")
                .param("pinNumber", "2002")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(accountFacade, times(1)).deleteAccount(any(), anyInt());
    }

    private AccountDto getSimpleAccountDto() {
        return new AccountDto(1L, BigDecimal.ZERO, 1L, Currency.EUR, "22 2222 2222 2222 2222 2222 2222",
                2222, LocalDate.now(), new ArrayList<>(), new ArrayList<>());
    }

}