package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    public List<AccountDTO> getAllAccounts();
    public ResponseEntity<Object> getAccount(Authentication authentication , Long id);
    public List<AccountDTO> getAll(Authentication authentication);
    public ResponseEntity<Object> createAccount (Authentication authentication);
    public String generateNumber(int min, int max);
}
