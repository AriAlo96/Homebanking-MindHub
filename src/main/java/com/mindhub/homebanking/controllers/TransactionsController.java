package com.mindhub.homebanking.controllers;

import com.itextpdf.text.DocumentException;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionsController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/clients/current/transfers")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam Double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originNumber,
                                                    @RequestParam String destinationNumber) {
        Client client = clientService.findClientByEmail(authentication.getName());
        Account accountDebit = accountService.findAccountByNumber(originNumber);
        Account accountCredit = accountService.findAccountByNumber(destinationNumber);

        if (accountDebit.getClient() != client) {
            return new ResponseEntity<>("The origin account doesn't belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit == null) {
            return new ResponseEntity<>("The origin account doesn't exist",
                    HttpStatus.FORBIDDEN);
        }
        if (accountCredit == null) {
            return new ResponseEntity<>("The destination account doesn't exist",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit.getBalance() < amount) {
            return new ResponseEntity<>("Your funds are insufficient",
                    HttpStatus.FORBIDDEN);
        }
        if (amount <= 0){
            return new ResponseEntity<>("The amount cannot be zero or negative",
                    HttpStatus.FORBIDDEN);
        }
        if (accountDebit.getNumber().equals(accountCredit.getNumber())) {
            return new ResponseEntity<>("The destination account cannot be the same as the origin account",
                    HttpStatus.FORBIDDEN);
        }
        if (description == null) {
            return new ResponseEntity<>("Description is required",
                    HttpStatus.FORBIDDEN);
        }
        if (amount == null) {
            return new ResponseEntity<>("Amount is required",
                    HttpStatus.FORBIDDEN);
        }
        if (originNumber.isBlank() || originNumber.isEmpty()) {
            return new ResponseEntity<>("Origin account is required",
                    HttpStatus.FORBIDDEN);
        }
        if (destinationNumber.isBlank() || destinationNumber.isEmpty()) {
            return new ResponseEntity<>("Destination account is required",
                    HttpStatus.FORBIDDEN);
        }

        double currentBalanceTransactionDebit = accountDebit.getBalance() - amount;
        boolean active = true;
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,
                (-amount), accountDebit.getNumber() + description,
                    LocalDateTime.now() , currentBalanceTransactionDebit , active);

        double currentBalanceTransactionCredit = accountCredit.getBalance() + amount;

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                amount, accountCredit.getNumber() + description,
                    LocalDateTime.now(), currentBalanceTransactionCredit, active);

            transactionService.saveTransaction(transactionDebit);
            accountDebit.addTransaction(transactionDebit);
            transactionService.saveTransaction(transactionCredit);
            accountCredit.addTransaction(transactionCredit);

            accountDebit.setBalance(accountDebit.getBalance() - amount);
            accountCredit.setBalance(accountCredit.getBalance() + amount);

            return new ResponseEntity<>("Transaction successfully", HttpStatus.CREATED);
        }

//    public ResponseEntity<Object> ExportingToPDF(HttpServletResponse response, Authentication authentication, String accountNumber, String startDate, String endingDate) throws DocumentException, IOException {
//        Client client = clientService.findClientByEmail(authentication.getName());
//        Account account = accountService.findAccountByNumber(accountNumber);
//
//        if (account == null) {
//            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
//        }
//
//        if (account.getClient() != client) {
//            return new ResponseEntity<>("The account doesn't belong to the authenticated client",
//                    HttpStatus.FORBIDDEN);
//        }
//
//        if (startDate.isBlank()) {
//            return new ResponseEntity<>("Start date cannot be empty", HttpStatus.FORBIDDEN);
//        } else if (endingDate.isBlank()) {
//            return new ResponseEntity<>("End date cannot be empty", HttpStatus.FORBIDDEN);
//        }
//
//        response.setContentType("application/pdf");
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=Transactions" + accountNumber + ".pdf";
//        response.setHeader(headerKey, headerValue);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate, formatter);
//        LocalDateTime dateTimeEnd = LocalDateTime.parse(endingDate, formatter);
//
//        List<Transaction> listTransactions = transactionService.searchByDate(client, accountNumber, dateTimeStart, dateTimeEnd);
//
//        TransactionPDF exporter = new TransactionPDF(listTransactions, account);
//        exporter.usePDFExport(response);
//
//        return new ResponseEntity<>("PDF created successfully", HttpStatus.CREATED);
//    }
    }



