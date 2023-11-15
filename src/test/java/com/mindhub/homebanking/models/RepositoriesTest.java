package com.mindhub.homebanking.models;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
    @Test
    public void existAccountByNumber(){
        Account account = accountRepository.findByNumber("VIN001");
        assertThat(account,is(notNullValue()));
    }
    @Test
    public void existAccountById(){
        Account account = accountRepository.findById(3L).orElse(null);
        assertThat(account,is(notNullValue()));
    }
    @Test
    public void existCardByTypeAndColorAndClient(){
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        Boolean card = cardRepository.existsByTypeAndColorAndClient(CardType.CREDIT, CardColor.TITANIUM , client);
        assertThat(card,is(true));
    }
    @Test
    public void existCardByNumber(){
        Boolean card = cardRepository.existsByNumber("1010 2584 6958 9387");
        assertThat(card,is(true));
    }
    @Test
    public void existClientByEmail(){
        Boolean client = clientRepository.existsClientByEmail("juanperez@savingsbank.com");
        assertThat(client,is(true));
    }
    @Test
    public void existFirstNameProperty(){
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(client, (hasProperty("firstName", is("Melba"))));
    }
    @Test
    public void existTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }
    @Test
    public void existDebitTransactionType(){
        List<Transaction> transaction = transactionRepository.findAll();
        assertThat(transaction, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
    }
}

