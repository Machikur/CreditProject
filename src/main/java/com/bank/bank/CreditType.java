package com.bank.bank;

import java.util.HashMap;
import java.util.Map;

public enum CreditType {
    WEEKLY(7), MONTHLY(30), SIX_MONTH(180), ANNUAL(365);

    private static final Map<Integer, CreditType> map;

    static {
        map = new HashMap<>();
        for (CreditType v : CreditType.values()) {
            map.put(v.getDays(), v);
        }
    }

    private final int days;

    CreditType(int days) {
        this.days = days;
    }

    public static CreditType findByKey(int i) {
        return map.get(i);
    }

    public int getDays() {
        return days;
    }

}
