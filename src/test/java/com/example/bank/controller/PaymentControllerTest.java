package com.example.bank.controller;

import com.example.bank.client.Currency;
import com.example.bank.dto.PaymentDto;
import com.example.bank.facade.PaymentFacade;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
    public void shouldCreatePaymentDto() throws Exception {
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
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId", is(1)))
                .andExpect(jsonPath("$.currency", is("PLN")));

    }

    @Test
    public void shouldReturnUsersListOfPaymentDto() throws Exception {
        //given
        PaymentDto paymentDto = getSimplePayment();
        when(paymentFacade.getPaymentListOfUser(anyLong())).thenReturn(Collections.singletonList(paymentDto));

        //when and then
        mockMvc.perform(get("/v1/userPayments")
                .contentType(MediaType.APPLICATION_JSON)
                 .param("userId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[].size()", is(1)))
                .andExpect(jsonPath("$[0].currency", is("PLN")));

    }

    @Test
    public void shouldReturnEmptyListOfPaymentDto() throws Exception {
        //given
        when(paymentFacade.getPaymentListOfAccount(anyLong())).thenReturn(new ArrayList<PaymentDto>());

        //when and then
        mockMvc.perform(get("/v1/accountPayments")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountId", "1")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));

    }


    private PaymentDto getSimplePayment() {
        return new PaymentDto(1L, 1L, 1L,
                null, Currency.PLN, null, BigDecimal.TEN);
    }

}