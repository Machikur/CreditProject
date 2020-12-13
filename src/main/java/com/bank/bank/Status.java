package com.bank.bank;

public enum Status {
    VIP(0.8), STANDARD(1), NEW(0.9);
    private final double interestMultiplier;

    Status(double interestMultiplier) {
        this.interestMultiplier = interestMultiplier;
    }


    public double getInterestMultiplier() {
        return interestMultiplier;
    }

}
