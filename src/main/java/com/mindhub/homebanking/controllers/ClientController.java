package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
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
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getAllClients(){
        List<ClientDTO> clients = clientService.findAllClients().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
        return clients;
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        ClientDTO client = new ClientDTO(clientService.findClientById(id));
        return client;
    }

    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || firstName.isEmpty()) {
            return new ResponseEntity<>("You must enter your name",
                    HttpStatus.FORBIDDEN);
        }

        if (lastName.isBlank() || lastName.isEmpty()) {
            return new ResponseEntity<>("You must enter your last name",HttpStatus.FORBIDDEN);
        }

        if (email.isBlank() || email.isEmpty()) {
            return new ResponseEntity<>("You must enter your email",HttpStatus.FORBIDDEN);
        }

        if (password.isBlank() || password.isEmpty()) {
            return new ResponseEntity<>("You must enter your password",HttpStatus.FORBIDDEN);
        }

        if (clientService.existsClientByEmail(email)) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        String accountNumber = checkAccountNumber();
        boolean active = true;
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(accountNumber, LocalDate.now(), 0, active , AccountType.SAVING);
        accountService.saveAccount(account);
        newClient.addAccount(account);
        clientService.saveClient(newClient);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    public String checkAccountNumber(){
        String numberGenerated;
        do{
            numberGenerated = AccountUtils.generateNumber();
        }while(accountService.existsAccountByNumber(numberGenerated));
        return numberGenerated;
    }
}
