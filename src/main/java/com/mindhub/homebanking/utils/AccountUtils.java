package com.mindhub.homebanking.utils;

public final class AccountUtils {
    private AccountUtils() {
    }
    public static String generateNumber() {
        String number = "VIN";
        return number + CardUtils.generateRandomNumber(100,99999999);
    }
}
