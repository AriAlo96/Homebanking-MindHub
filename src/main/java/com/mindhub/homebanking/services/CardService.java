package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CardService {
   boolean existsCardByTypeAndColorAndClientAndActive (CardType type, CardColor color, Client client , boolean active);
   void saveCard (Card card);
   boolean existsCardByNumber (String number);
   Card findById (Long id);
   List<Card> findAll();
}
