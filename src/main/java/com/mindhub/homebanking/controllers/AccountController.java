package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        List<AccountDTO> accounts = accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        AccountDTO foundAccount = accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
        return foundAccount;
    }

    @PostMapping("/clients/current/accounts")
    public  ResponseEntity<Object> createAccount (Authentication authentication) {
        Client client = (clientRepository.findByEmail(authentication.getName()));
        if (client == null) {
            throw new UsernameNotFoundException("Unknow client " + authentication.getName());
        }
        if (client.getAccounts().size() <3){
        Account account = new Account(generateNumber(1, 100000000), LocalDate.now(), 0);
        accountRepository.save(account);
        client.addAccount(account);
        clientRepository.save(client);
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
} else {
            return new ResponseEntity<>("You have reached the limit of created accounts", HttpStatus.FORBIDDEN);
        }
    }
    private String generateNumber(int min, int max) {
        List<AccountDTO> accounts = getAllAccounts();
        Set<String> setAccounts = accounts.stream().map(accountDTO ->
            accountDTO.getNumber()
        ).collect(Collectors.toSet());
        String aux = "VIN";
       long number;
       String numbercompleted;
       do{
           number = (int) ((Math.random() * (max - min)) + min);
           String formattedNumber = String.format("%03d", number);
           numbercompleted = aux + formattedNumber;
       } while (setAccounts.contains(numbercompleted));
       return  numbercompleted;
    }
    }