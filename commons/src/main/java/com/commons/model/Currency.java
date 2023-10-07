package com.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {

    RUB("₽"),
    USD("$"),
    EUR("€"),
    UAH("₴"),
    PLN("zl"),
    GBP("£");

    private final String sign;

    public static String getSignFromTitle(String value) {
        for (Currency currency : values()) {
            if (currency.name().equals(value)) {
                return currency.getSign();
            }
        }
        return value;
    }
}
