package com.example.bank.dto;

import com.example.bank.bank.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String mailAddress;

    private List<AccountDto> accounts;

    private List<CreditDto> credits;

    private Double monthlyEarnings;

    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;


}
