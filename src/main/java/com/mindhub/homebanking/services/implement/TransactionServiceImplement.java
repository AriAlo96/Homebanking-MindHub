package com.mindhub.homebanking.services.implement;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> searchByDate(Client client,
                                          String accountNumber,
                                          LocalDateTime startDate,
                                          LocalDateTime endingDate) {
        Account account= accountService.findAccountByNumber(accountNumber);
        List<Transaction> listTransactions = new ArrayList<>(); for (Transaction transaction : account.getTransactions()) {
            if (transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endingDate)) {
                listTransactions.add(transaction);
            }
        }
        return listTransactions;
    }

}
