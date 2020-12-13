package com.bank.controller;

import com.bank.client.currency.Quotes;
import com.bank.client.currency.Rates;
import com.bank.service.CurrencyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void shouldReturnQuotes() throws Exception {
        //given
        Quotes quotes = new Quotes("PLN", new Rates(2, 2, 2, 2));
        when(currencyService.findActualExchangeRates(any())).thenReturn(quotes);

        //when and then
        mockMvc.perform(get("/v1/exchangeRates")
                .param("currency", "PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base", is("PLN")))
                .andExpect(jsonPath("$.rates.PLN", is(2.0)));

        verify(currencyService, times(1)).findActualExchangeRates(any());

    }

    @Test
    public void shouldReturnDifferentQuote() throws Exception {
        //given
        when(currencyService.findExchangeQuote(any(), any(), any())).thenReturn(BigDecimal.TEN);

        //when and then
        mockMvc.perform(get("/v1/exchangeQuote")
                .param("from", "PLN")
                .param("to", "EUR")
                .param("quote", "45")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(10)));

        verify(currencyService, times(1)).findExchangeQuote(any(), any(), any());
    }

}