package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public boolean existsCardByTypeAndColorAndClient(CardType type,
                                                     CardColor color,
                                                     Client client) {
        return cardRepository.existsByTypeAndColorAndClient(type,color,client);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean existsCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }
}
