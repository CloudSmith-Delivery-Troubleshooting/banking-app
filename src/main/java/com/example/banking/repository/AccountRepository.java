package com.example.banking.repository;

import com.example.banking.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }
}
