package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<AccountDTO> getAllAccounts(){
        List<AccountDTO> accounts = accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }
    @Override
    public ResponseEntity<Object> getAccount(Authentication authentication , @PathVariable Long id){
        Client client = (clientRepository.findByEmail(authentication.getName()));
        Set<Long> accountsId = client.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet());
        if (!accountsId.contains(id)) {
            return new ResponseEntity<>("the account does not belong to the authenticated client" , HttpStatus.FORBIDDEN);
        }
        AccountDTO foundAccount = accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
        return new ResponseEntity<>(foundAccount,HttpStatus.CREATED);
    }
    @Override
    public List<AccountDTO> getAll(Authentication authentication) {
        Client client = (clientRepository.findByEmail(authentication.getName()));
        List<AccountDTO> accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return accounts;
    }
    @Override
    public ResponseEntity<Object> createAccount (Authentication authentication) {
        Client client = (clientRepository.findByEmail(authentication.getName()));
        if (client == null) {
            return new ResponseEntity<>("The client was not found", HttpStatus.NOT_FOUND);
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
    @Override
    public String generateNumber(int min, int max) {
        String aux = "VIN";
        long number;
        String numbercompleted;
        do{
            number = (int) ((Math.random() * (max - min)) + min);
            String formattedNumber = String.format("%03d", number);
            numbercompleted = aux + formattedNumber;
        } while (accountRepository.existsByNumber(numbercompleted));
        return  numbercompleted;
    }
}
