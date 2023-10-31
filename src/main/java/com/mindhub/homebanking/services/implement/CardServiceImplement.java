package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Override
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        Client client = (clientRepository.findByEmail(authentication.getName()));
        if (client == null) {
            throw new UsernameNotFoundException("Unknow client " + authentication.getName());
        }
        if ((client.getCards().stream().filter(card -> card.getType() == type).count() < 3)) {
            Card card = new Card((client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase()), type,
                    color, generateNumber(1, 10000) ,  generateCvv(1, 1000) , LocalDate.now().plusYears(5), LocalDate.now());
            cardRepository.save(card);
            client.addCard(card);
            clientRepository.save(client);
            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("You have reached the limit of created cards", HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public String generateNumber(int min, int max) {
        long number1;
        long number2;
        long number3;
        long number4;
        String numberCompleted;
        do{
            number1 = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber1 = String.format("%04d", number1);
            number2 = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber2 = String.format("%04d", number2);
            number3 = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber3 = String.format("%04d", number3);
            number4 = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber4 = String.format("%04d", number4);
            numberCompleted = formattedNumber1 + " " + formattedNumber2 + " " + formattedNumber3 + " " + formattedNumber4;
        } while (cardRepository.existsByNumber(numberCompleted));
        return  numberCompleted;
    }
    @Override
    public String generateCvv(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        String formattedNumber = String.format("%03d", number);
        return  formattedNumber;
    }
}
