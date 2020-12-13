package com.bank.client.currency;

public enum Currency {
    PLN("PLN"), USD("USD"), EUR("EUR"), GBP("GBP");

    private final String desc;

    Currency(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
