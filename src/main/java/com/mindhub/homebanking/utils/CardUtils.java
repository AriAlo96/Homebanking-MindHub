package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {
    private CardUtils() {
    }
    public static int generateRandomNumber (int min,int max){
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static String generateNumber() {
            String number = "";
        for (int i = 0; i < 3 ; i++) {
            number += generateRandomNumber(1000,9999) + " ";
        } number += generateRandomNumber(1000,9999);
        return  number;
    }
    public static int generateCvv() {
        int number = generateRandomNumber(100,999);
        return  number;
    }

}
