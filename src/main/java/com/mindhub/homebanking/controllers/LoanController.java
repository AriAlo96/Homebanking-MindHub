package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping ("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getAllLoans(Authentication authentication) {
        Client client = clientService.findClientByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            List<LoanDTO> loans = loanService.findAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
            return ResponseEntity.ok(loans);
        }
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan (Authentication authentication, @RequestBody
                                                LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Loan loan = loanService.findLoanById(loanApplicationDTO.getLoanId());
        Account account = accountService.findAccountByNumber(loanApplicationDTO.getDestinationAccount());
        if (client == null) {
            return new ResponseEntity<>("Unknow client " + authentication.getName(),
                    HttpStatus.UNAUTHORIZED);
        }
        if (loanApplicationDTO.getLoanId() == 0) {
            return new ResponseEntity<>("The type of loan is required", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() == 0) {
            return new ResponseEntity<>("The amount of loan is required", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("The amount cannot be zero or negative",
                    HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() == 0) {
            return new ResponseEntity<>("The number of payments is required", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getDestinationAccount().isBlank()) {
            return new ResponseEntity<>("The destination account is required", HttpStatus.FORBIDDEN);
        }
        if(loan == null){
            return new ResponseEntity<>("The loan doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The requested amount exceeds the maximum loan amount", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The amount of payments is incorrect", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("The destination account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The destination account doesn´t belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (loan.getclients().contains(client)){
            return new ResponseEntity<>("You have already applied for this loan", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20,
                loanApplicationDTO.getPayments());

        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        double currentBalanceAccountCredit = account.getBalance() + loanApplicationDTO.getAmount();

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                loanApplicationDTO.getAmount(), loan.getName() + " Loan approved",
                LocalDateTime.now() , currentBalanceAccountCredit,true);

        transactionService.saveTransaction(transactionCredit);
        account.addTransaction(transactionCredit);
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
        }
}
