package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;


public interface TransactionService {
    void saveTransaction(Transaction transaction);
    public List<Transaction> searchByDate (Client client, String accountNumber, LocalDateTime startDate, LocalDateTime endingDate);

}
