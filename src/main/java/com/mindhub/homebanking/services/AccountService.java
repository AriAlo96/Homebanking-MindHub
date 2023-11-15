package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    List<Account> findAllAccounts();
    Account findAccountById(Long id);
    void saveAccount(Account account);
    boolean existsAccountByNumber (String number);
    Account findAccountByNumber (String number);
    Account findById (Long id);
}
