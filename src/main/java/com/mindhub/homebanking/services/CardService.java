package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

public interface CardService {
   boolean existsCardByTypeAndColorAndClient (CardType type, CardColor color, Client client);
   void saveCard (Card card);
   boolean existsCardByNumber (String number);
}
