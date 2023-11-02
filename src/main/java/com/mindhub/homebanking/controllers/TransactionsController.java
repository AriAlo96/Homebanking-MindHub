package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionsController {
    @Autowired
    private TransactionService transactionService;
    @Transactional
    @PostMapping("/clients/current/transfers")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originNumber,
                                                    @RequestParam String destinationNumber) {return transactionService.createTransaction(authentication, amount, description, originNumber, destinationNumber);}
}

