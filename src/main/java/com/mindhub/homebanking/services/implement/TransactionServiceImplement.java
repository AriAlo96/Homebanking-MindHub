package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private com.mindhub.homebanking.repositories.TransactionRepository TransactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originNumber,
                                                    @RequestParam String destinationNumber) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountDebit = accountRepository.findByNumber(originNumber);
        Account accountCredit = accountRepository.findByNumber(destinationNumber);
        if (client == null) {
            throw new UsernameNotFoundException("Unknow client " + authentication.getName());
        }

        if (accountDebit.getClient() != client) {
            return new ResponseEntity<>("The origin account doesn´t belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit == null) {
            return new ResponseEntity<>("The origin account doesn´t exist",
                    HttpStatus.FORBIDDEN);
        }
        if (accountCredit == null) {
            return new ResponseEntity<>("The destination account doesn´t exist",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit.getBalance() < amount) {
            return new ResponseEntity<>("Your funds are insufficient",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit.getNumber() == accountCredit.getNumber()) {
            return new ResponseEntity<>("the destination account cannot be the same as the origin account",
                    HttpStatus.FORBIDDEN);
        } else {
            Transaction transactionDebit = new Transaction(TransactionType.DEBIT,
                    (-amount),
                    accountDebit.getNumber() + description,
                    LocalDateTime.now());

            Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                    amount,
                    accountCredit.getNumber() + description,
                    LocalDateTime.now());

            TransactionRepository.save(transactionDebit);
            accountDebit.addTransaction(transactionDebit);
            TransactionRepository.save(transactionCredit);
            accountCredit.addTransaction(transactionCredit);

            accountDebit.setBalance(accountDebit.getBalance() - amount);
            accountCredit.setBalance(accountCredit.getBalance() + amount);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
