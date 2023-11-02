package com.mindhub.homebanking.dtos;


public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long loanId,
                              double amount,
                              int payments,
                              String destinationAccount) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
