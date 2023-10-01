package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository) {
		return args -> {
			Client client1 = new Client ("Mabel" , "Morel" , "melba@mindhub.com");
			Client client2 = new Client ("Juan" , "Perez" , "juanperez@mindhub.com");

			clientRepository.save(client1);
			clientRepository.save(client2);
		};
	}
}