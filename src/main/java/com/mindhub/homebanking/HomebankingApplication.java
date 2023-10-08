package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository , AccountRepository accountRepository , TransactionRepository transactionRepository) {
		return args -> {
			Client client1 = new Client ("Melba" , "Morel" , "melba@mindhub.com");
			Client client2 = new Client ("Juan" , "Perez" , "juanperez@mindhub.com");
			clientRepository.save(client1);
			clientRepository.save(client2);

			Account account1 = new Account ("VIN001" , LocalDate.now(), 5000);
			Account account2 = new Account ("VIN002" , LocalDate.now().plusDays(1), 7500);
			Account account3 = new Account ("VIN003" , LocalDate.now(), 5800);
			Account account4 = new Account ("VIN004" , LocalDate.now().plusDays(1), 9400);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(DEBIT , -7836.7 , "Supermarket" , LocalDateTime.now());
			Transaction transaction2 = new Transaction(CREDIT , 9620.3 , "Deposit" , LocalDateTime.now().plusHours(8).plusMinutes(32));
			Transaction transaction3 = new Transaction(DEBIT , -30000 , "Rent" , LocalDateTime.now().plusHours(21).plusMinutes(5));
			Transaction transaction4 = new Transaction(DEBIT , -12400 , "School" , LocalDateTime.now().plusDays(2).plusMinutes(49));
			Transaction transaction5 = new Transaction(CREDIT , 17500 , "Deposit" , LocalDateTime.now().plusDays(3).plusHours(5).plusMinutes(12));
			Transaction transaction6 = new Transaction(CREDIT , 12300.25 , "Deposit" , LocalDateTime.now().plusMinutes(29));
			Transaction transaction7 = new Transaction(DEBIT , -9300 , "Shopping" , LocalDateTime.now().plusDays(6));
			Transaction transaction8 = new Transaction(DEBIT , -20000 , "Supermarket" , LocalDateTime.now().plusMinutes(47));
			Transaction transaction9 = new Transaction(DEBIT , -28569 , "School" , LocalDateTime.now().plusDays(1));
			Transaction transaction10 = new Transaction(DEBIT , -45000 , "Rent" , LocalDateTime.now().plusMinutes(25));
			Transaction transaction11 = new Transaction(CREDIT , 4200 , "Deposit" , LocalDateTime.now().plusMinutes(41));
			Transaction transaction12 = new Transaction(CREDIT , 6890 , "Deposit" , LocalDateTime.now().plusDays(2));
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);
			account2.addTransaction(transaction6);
			account2.addTransaction(transaction7);
			account2.addTransaction(transaction8);
			account3.addTransaction(transaction9);
			account3.addTransaction(transaction10);
			account4.addTransaction(transaction11);
			account4.addTransaction(transaction12);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);
		};
	}
}