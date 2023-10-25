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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getAllClients(){
    List<ClientDTO> clients = clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    return clients;
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        ClientDTO foundClient = clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
        return foundClient;
    }

    @RequestMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        StringBuilder errors = new StringBuilder();
        if (firstName.isBlank()) {
            errors.append("You must enter your name\n");
        }

        if (lastName.isBlank()) {
            errors.append("You must enter your last name\n");
        }

        if (email.isBlank()) {
            errors.append("You must enter your email\n");
        }

        if (password.isBlank()) {
            errors.append("You must enter your password\n");
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        if (errors.length() > 0) {
            return new ResponseEntity<>(errors.toString(), HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(generateNumber(1, 100000000), LocalDate.now(), 0);
        accountRepository.save(account);
        newClient.addAccount(account);
        clientRepository.save(newClient);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    private String generateNumber(int min, int max) {
        List<AccountDTO> accounts = accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
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
