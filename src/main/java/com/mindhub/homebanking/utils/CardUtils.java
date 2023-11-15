package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {
    private CardUtils() {
    }
    public static String generateNumber() {
            int number1 = (int) ((Math.random() * (9999 - 1000)) + 1000);
            int number2 = (int) ((Math.random() * (9999 - 1000)) + 1000);
            int number3 = (int) ((Math.random() * (9999 - 1000)) + 1000);
            int number4 = (int) ((Math.random() * (9999 - 1000)) + 1000);
            String generatedNumber = number1 + " " + number2 + " " + number3 + " " + number4;
        return  generatedNumber;
    }
    public static int generateCvv() {
        int number = (int) ((Math.random() * (999 - 100)) + 100);
        return  number;
    }
}
