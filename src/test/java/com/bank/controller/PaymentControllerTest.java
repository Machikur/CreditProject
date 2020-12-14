package com.bank.controller;

import com.bank.client.currency.Currency;
import com.bank.dto.PaymentDto;
import com.bank.facade.PaymentFacade;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentFacade paymentFacade;

    @Test
    public void shouldCreatePaymentDtoTest() throws Exception {
        //given
        PaymentDto paymentDto = getSimplePayment();
        when(paymentFacade.makePayment(any(PaymentDto.class), anyInt())).thenReturn(paymentDto);

        Gson gson = new Gson();
        String paymentToGson = gson.toJson(paymentDto);

        //when and then
        mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentToGson)
                .param("pinNumber", "1223")
                .param("userId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId", is(1)))
                .andExpect(jsonPath("$.currency", is("PLN")));

        verify(paymentFacade, times(1)).makePayment(any(), anyInt());
    }

    @Test
    public void shouldReturnUsersListOfPaymentDtoTest() throws Exception {
        //given
        PaymentDto paymentDto = getSimplePayment();
        when(paymentFacade.getPaymentListOfUser(anyLong())).thenReturn(Collections.singletonList(paymentDto));

        //when and then
        mockMvc.perform(get("/v1/userPayments")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].currency", is("PLN")));

        verify(paymentFacade, times(1)).getPaymentListOfUser(anyLong());

    }

    @Test
    public void shouldReturnEmptyListOfPaymentDtoTest() throws Exception {
        //given
        when(paymentFacade.getPaymentListOfAccount(anyString())).thenReturn(new ArrayList<>());

        //when and then
        mockMvc.perform(get("/v1/accountPayments")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", "123")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));

        verify(paymentFacade, times(1)).getPaymentListOfAccount(anyString());
    }

    private PaymentDto getSimplePayment() {
        return new PaymentDto(1L, 1L, 1L,
                null, Currency.PLN, null, BigDecimal.TEN);
    }

}