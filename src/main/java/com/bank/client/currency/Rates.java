package com.bank.client.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Rates {

    @JsonProperty("GBP")
    private double GBP;

    @JsonProperty("PLN")
    private double PLN;

    @JsonProperty("EUR")
    private double EUR;

    @JsonProperty("USD")
    private double USD;

    public double getRate(Currency currency) {
        switch (currency) {
            case GBP:
                return this.GBP;
            case PLN:
                return this.PLN;
            case EUR:
                return this.EUR;
            case USD:
                return this.USD;
            default:
                throw new IllegalArgumentException("Wrong Currency");
        }
    }

}
