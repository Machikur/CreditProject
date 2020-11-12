package com.example.credit.bank;

public enum Status {
    VIP("VIP"), STANDARD("STANDARD"), NEW("NEW");

    private String desc;

    Status(String desc) {
        this.desc = desc.toUpperCase();
    }

}
