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
import java.text.DecimalFormat;


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

        double amount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * loan.getInterestPercentage());
        Double currentAmount = amount;
        ClientLoan clientLoan = new ClientLoan(amount,loanApplicationDTO.getPayments(),currentAmount,
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
        account.setBalance(currentBalanceAccountCredit);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
        }

    @PostMapping("/admin/loans")
    public ResponseEntity<Object> createNewLoanType (Authentication authentication, @RequestParam String name , @RequestParam Double maxAmount , @RequestParam List<Integer> payments , @RequestParam Double interestPercentage) {
        Client client = clientService.findClientByEmail(authentication.getName());
        if(name.isEmpty() || name.isBlank()){
            return new ResponseEntity<>("The name is required", HttpStatus.FORBIDDEN);
        }
        if(maxAmount == null){
            return new ResponseEntity<>("The maximum amount is required", HttpStatus.FORBIDDEN);
        }
        if(payments.isEmpty()){
            return new ResponseEntity<>("Payments are required", HttpStatus.FORBIDDEN);
        }
        if(interestPercentage == null){
            return new ResponseEntity<>("The interest percentage is required", HttpStatus.FORBIDDEN);
        }

        if(loanService.existsByName(name)){
            return new ResponseEntity<>("This loan already exists", HttpStatus.FORBIDDEN);
        }
        if (maxAmount <=0){
            return new ResponseEntity<>("The maximum amount cannot be less than or equal to zero", HttpStatus.FORBIDDEN);
        }
        Loan newLoan = new Loan(name,maxAmount,payments,interestPercentage);

        loanService.saveLoan(newLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/loans/payments")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam long idLoan , @RequestParam long idAccount ,
                                          @RequestParam Double amount){
    Client client = clientService.findClientByEmail(authentication.getName());
    ClientLoan clientLoan = clientLoanService.findById(idLoan);
    Account account = accountService.findAccountById(idAccount);
    String loan = clientLoan.getLoan().getName();
    double installmentValue = clientLoan.getAmount() / clientLoan.getPayments();
    double roundedInstallmentValue = Math.round(installmentValue * 100.0) / 100.0;

    if (!clientLoan.getClient().equals(client)){
        return new ResponseEntity<>("The loan doesn't belong to the authenticated client",
                HttpStatus.FORBIDDEN);
    }
    if (account == null) {
        return new ResponseEntity<>("The account doesn´t exist",
                HttpStatus.FORBIDDEN);
    }
    if (!account.getClient().equals(client)){
        return new ResponseEntity<>("The account doesn't belong to the authenticated client",
                HttpStatus.FORBIDDEN);
    }
    if (amount != roundedInstallmentValue){
        return new ResponseEntity<>("The amount entered does not correspond to the payment of 1 installment. Your amount to pay is US$ " + roundedInstallmentValue,
                HttpStatus.FORBIDDEN);
    }
    if (amount == null) {
        return new ResponseEntity<>("Amount is required",
                HttpStatus.FORBIDDEN);
    }
    if (amount <= 0){
        return new ResponseEntity<>("The amount cannot be zero or negative",
                HttpStatus.FORBIDDEN);
    }
    if (account.getBalance() < amount) {
        return new ResponseEntity<>("Your funds are insufficient",
                HttpStatus.FORBIDDEN);
    }

    double currentBalanceAccountDebit = account.getBalance() - amount;
    Transaction transaction = new Transaction(TransactionType.DEBIT,amount,"Canceled fee " + loan + " loan",LocalDateTime.now(),currentBalanceAccountDebit,true);

    clientLoan.setCurrentAmount(clientLoan.getCurrentAmount()-amount);
    clientLoan.setCurrentPayments(clientLoan.getCurrentPayments()-1);
    account.setBalance(account.getBalance()-amount);
    account.addTransaction(transaction);
    clientLoanService.saveClientLoan(clientLoan);
    transactionService.saveTransaction(transaction);
    accountService.saveAccount(account);
    return new ResponseEntity<>("Payment made successfully",HttpStatus.CREATED);
    }

}
