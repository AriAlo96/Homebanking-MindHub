package com.mindhub.homebanking.utils;

public final class AccountUtils {
    private AccountUtils() {
    }
    public static String generateNumber() {
        long number = (int) ((Math.random() * (99999999 - 100)) + 100);
        String numbercompleted = "VIN" + number;
        return numbercompleted;
    }
}
