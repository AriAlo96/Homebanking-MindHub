package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

public interface CardService {
    public ResponseEntity<Object> createCard(Authentication authentication, CardType type, CardColor color);
    public String generateNumber(int min, int max);
    public String generateCvv(int min, int max);

}
