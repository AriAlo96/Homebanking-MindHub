package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long loanId;
    private double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long loanId,
                              double amount,
                              int payments,
                              String destinationAccount) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public long getLoanId() {
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
