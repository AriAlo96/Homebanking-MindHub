package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {
    List<Client> findAllClients();
    Client findClientById (Long id);
    Client findClientByEmail (String email);
    void saveClient (Client client);
    boolean existsClientByEmail(String email);
}
