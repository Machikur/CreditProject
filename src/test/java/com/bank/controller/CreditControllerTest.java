package com.bank.controller;

import com.bank.bank.CreditType;
import com.bank.client.currency.Currency;
import com.bank.dto.CreditDto;
import com.bank.dto.CreditOptionsDto;
import com.bank.facade.CreditFacade;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CreditController.class)
public class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditFacade creditFacade;

    @Test
    public void postMappingTest() throws Exception {

        //when and then
        mockMvc.perform(post("/v1/credit")
                .param("accountId", "1")
                .param("userId", "1")
                .param("quote", "1")
                .param("days", "1")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(creditFacade, times(1)).createCreditForUser(anyLong(), anyLong(), any(), any());

    }


    @Test
    public void getUserCreditsTest() throws Exception {
        //given
        List<CreditDto> creditDtos = Arrays.asList(getSimpleCreditDto(), getSimpleCreditDto(), getSimpleCreditDto());
        when(creditFacade.getCreditsForUser(any())).thenReturn(creditDtos);

        //when and then\
        mockMvc.perform(get("/v1/credits")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].creditId", is(1)))
                .andExpect(jsonPath("$[0].finished", is(false)))
                .andExpect(jsonPath("$[0].currency", is(Currency.PLN.getDesc())));

        verify(creditFacade, times(1)).getCreditsForUser(anyLong());
    }

    @Test
    public void getCreditInterestTest() throws Exception {
        //given
        when(creditFacade.countInterest(anyLong(), anyInt())).thenReturn(10.0);

        //when and then\
        mockMvc.perform(get("/v1/credit/interest")
                .param("userId", "1")
                .param("days", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("10.0"));
        verify(creditFacade, times(1)).countInterest(anyLong(), anyInt());
    }

    @Test
    public void deleteCreditTest() throws Exception {

        //when and then\
        mockMvc.perform(delete("/v1/credit")
                .param("creditId", "1")
                .accept(MediaType.ALL))
                .andExpect(status().isOk());

        verify(creditFacade, times(1)).deleteCredit(anyLong());
    }

    @Test
    public void userOptionsTest() throws Exception {
        //given
        CreditOptionsDto creditOptionsDto = new CreditOptionsDto(BigDecimal.TEN, Arrays.asList(CreditType.values()));
        when(creditFacade.getOptionsForUser(anyLong())).thenReturn(creditOptionsDto);

        //when and then
        mockMvc.perform(get("/v1/userOptions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1"))
                .andExpect(jsonPath("$.maxQuote", is(BigDecimal.TEN.intValue())))
                .andExpect(jsonPath("$.availableCreditTypes.size()", is(4)));

        verify(creditFacade, (times(1))).getOptionsForUser(anyLong());
    }

    private CreditDto getSimpleCreditDto() {
        return new CreditDto(1L, 1L, BigDecimal.TEN, BigDecimal.ZERO,
                Currency.PLN, LocalDate.now().plusDays(1), LocalDate.now(), false, new ArrayList<>());
    }

}