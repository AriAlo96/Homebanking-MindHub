package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        if (client == null) {
            throw new UsernameNotFoundException("Unknow client " + authentication.getName());
        }
        if (cardService.existsCardByTypeAndColorAndClient(type, color, client)) {
            return new ResponseEntity<>("You already have the same card", HttpStatus.FORBIDDEN);
        }
        Card card = new Card((client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase()), type,
                color, generateNumber(1, 10000) ,  generateCvv(1, 1000) , LocalDate.now().plusYears(5), LocalDate.now());
        cardService.saveCard(card);
        client.addCard(card);
        clientService.saveClient(client);
        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }
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
        } while (cardService.existsCardByNumber(numberCompleted));
        return  numberCompleted;
    }
    public String generateCvv(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        String formattedNumber = String.format("%03d", number);
        return  formattedNumber;
    }

}

