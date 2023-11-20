package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accounts = accountService.findAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(Authentication authentication,
                                             @PathVariable Long id) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        Set<Long> accountsId = client.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet());
        Account account = accountService.findAccountById(id);
        if (!accountsId.contains(id)) {
            return new ResponseEntity<>("the account does not belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account not found",
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new AccountDTO(account),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAll(Authentication authentication) {
        ClientDTO client = new ClientDTO(clientService.findClientByEmail(authentication.getName()));
        Set<AccountDTO> accounts = client.getAccounts();
        return accounts;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication , @RequestParam
                                                AccountType accountType) {
        Client client = (clientService.findClientByEmail(authentication.getName()));
        if (!clientService.existsClientByEmail(authentication.getName())) {
            return new ResponseEntity<>("The client was not found",
                    HttpStatus.NOT_FOUND);
        }
        if (!accountService.existsByActive(true)){
            return new ResponseEntity<>("This account is already active",
                    HttpStatus.FORBIDDEN);
        }
        String accountNumber = checkAccountNumber();
        List<Account> acountsActive = client.getAccounts().stream().filter(account -> account.getActive()).collect(Collectors.toList());
        if (acountsActive.size() > 3) {
            return new ResponseEntity<>("You have reached the limit of created accounts",
                    HttpStatus.FORBIDDEN);
        }
        boolean active = true;
        Account account = new Account(accountNumber, LocalDate.now(), 0 , active , accountType);
        accountService.saveAccount(account);
        client.addAccount(account);
        clientService.saveClient(client);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
        }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam Long id) {
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null) {
            return new ResponseEntity<>("The account doesn't exist",
                    HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() != 0) {
            return new ResponseEntity<>("You cannot delete an account with a balance greater than zero",
                    HttpStatus.FORBIDDEN);
        }
        if (!account.getActive()) {
            return new ResponseEntity<>("The account is inactive",
                    HttpStatus.FORBIDDEN);
        }
        if (!account.getClient().equals(client)) {
            return new ResponseEntity<>("The account doesn't belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        account.getTransactions().forEach(transaction -> transaction.setActive(false));
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.CREATED);
    }

    public String checkAccountNumber(){
        String numberGenerated;
        do{
            numberGenerated = AccountUtils.generateNumber();
        }while(accountService.existsAccountByNumber(numberGenerated));
        return numberGenerated;
    }
}