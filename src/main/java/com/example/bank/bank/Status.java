package com.example.bank.bank;

public enum Status {
    VIP("VIP", 0.8), STANDARD("STANDARD", 1), NEW("NEW", 0.9);
    private String desc;
    private double interestMultiplier;

    Status(String desc, double interestMultiplier) {
        this.desc = desc;
        this.interestMultiplier = interestMultiplier;
    }

    public String getDesc() {
        return desc;
    }

    public double getInterestMultiplier() {
        return interestMultiplier;
    }
}
