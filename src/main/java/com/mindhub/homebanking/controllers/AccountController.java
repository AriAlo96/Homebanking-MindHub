package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accounts = accountService.findAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(Authentication authentication,
                                             @PathVariable Long id) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        Set<Long> accountsId = client.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet());
        Account account = accountService.findAccountById(id);
        if (!accountsId.contains(id)) {
            return new ResponseEntity<>("the account does not belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }
        if (account != null) {
            return new ResponseEntity<>(new AccountDTO(account),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAll(Authentication authentication) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        List<AccountDTO> accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        if (!clientService.existsClientByEmail(authentication.getName())) {
            return new ResponseEntity<>("The client was not found",
                    HttpStatus.NOT_FOUND);
        }
        if (client.getAccounts().size() > 3) {
            return new ResponseEntity<>("You have reached the limit of created accounts",
                    HttpStatus.FORBIDDEN);
        } else {
            Account account = new Account(generateNumber(1,
                    100000000),
                    LocalDate.now(),
                    0);
            accountService.saveAccount(account);
            client.addAccount(account);
            clientService.saveClient(client);
            return new ResponseEntity<>("Account created successfully",
                    HttpStatus.CREATED);
        }
    }

    public String generateNumber(int min,
                                 int max) {
        String aux = "VIN";
        long number;
        String numbercompleted;
        do {
            number = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber = String.format("%03d",
                    number);
            numbercompleted = aux + formattedNumber;
        } while (accountService.existsAccountByNumber(numbercompleted));
        return numbercompleted;
    }
}