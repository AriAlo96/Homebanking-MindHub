package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {
    public List<ClientDTO> getAllClients();
    public ClientDTO getClient(Long id);
    public ClientDTO getAll(Authentication authentication);
    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password);
    public String generateNumber(int min, int max);
}
