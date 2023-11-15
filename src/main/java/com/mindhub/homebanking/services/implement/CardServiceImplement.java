package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public boolean existsCardByTypeAndColorAndClientAndActive(CardType type,
                                                     CardColor color,
                                                     Client client , boolean active) {
        return cardRepository.existsByTypeAndColorAndClientAndActive(type,color,client,active);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean existsCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }


}
