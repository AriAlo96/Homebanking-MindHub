package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping ("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getAllLoans(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            List<LoanDTO> loans = loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
            return ResponseEntity.ok(loans);
        }
    }

//    @Transactional
//    @PostMapping("/loans")
//    public ResponseEntity<Object> applyForLoan (Authentication authentication, @RequestBody
//                                                LoanApplicationDTO loanApplicationDTO) {
//        Client client = clientRepository.findByEmail(authentication.getName());
//        Optional<Loan> loan = loanRepository.findById(loanApplicationDTO.getLoanId());
//        Account account = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccount());
//        if (client == null) {
//            return new ResponseEntity<>("Unknow client " + authentication.getName(),
//                    HttpStatus.UNAUTHORIZED);
//        }
//        if (loanApplicationDTO.getLoanId() == 0) {
//            return new ResponseEntity<>("The type of loan is required", HttpStatus.FORBIDDEN);
//        }
//        if (loanApplicationDTO.getAmount() == 0) {
//            return new ResponseEntity<>("The amount of loan is required", HttpStatus.FORBIDDEN);
//        }
//        if (loanApplicationDTO.getPayments() == 0) {
//            return new ResponseEntity<>("The number of payments is required", HttpStatus.FORBIDDEN);
//        }
//        if (loanApplicationDTO.getDestinationAccount().isBlank()) {
//            return new ResponseEntity<>("The destination account is required", HttpStatus.FORBIDDEN);
//        }
//
//        }
}
