package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    @OneToMany(mappedBy = "account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDate creationDate, double balance , boolean active) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void addTransaction (Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number=" + number +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}
