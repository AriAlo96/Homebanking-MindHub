package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){return accountService.getAllAccounts();}
    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(Authentication authentication , @PathVariable Long id){return accountService.getAccount(authentication,id);}
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAll(Authentication authentication) {return accountService.getAll(authentication);}
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount (Authentication authentication) {return accountService.createAccount(authentication);}
    public String generateNumber(int min, int max) {return accountService.generateNumber(min, max);}
    }