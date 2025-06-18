package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository repo) {
        this.accountRepository = repo;
    }

    public Account createAccount(String accountNumber, String holderName, double initialBalance) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (holderName == null || holderName.isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new IllegalArgumentException("Account number already exists");
        }
        Account account = new Account(accountNumber, holderName, initialBalance);
        accountRepository.save(account);
        return account;
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.withdraw(amount);
    }

    public void transfer(String fromAccount, String toAccount, double amount) {
        if (fromAccount == null) {
            throw new NullPointerException("Source account not found");
        }
        if (toAccount == null) {
            throw new NullPointerException("Destination account not found");
        }
        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account source = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account destination = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        source.withdraw(amount);
        destination.deposit(amount);
    }

    public double getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.getBalance();
    }

    public List<com.example.banking.model.Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.getTransactions();
    }
}
