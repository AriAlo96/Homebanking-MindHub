package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> findAllLoans ();
    Loan findLoanById (Long id);
}
