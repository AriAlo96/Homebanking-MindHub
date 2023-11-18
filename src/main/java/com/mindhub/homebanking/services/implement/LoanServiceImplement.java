package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    LoanRepository loanRepository;
    @Override
    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }
    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public boolean existsByName(String name) {
        return loanRepository.existsByName(name);
    }
}
