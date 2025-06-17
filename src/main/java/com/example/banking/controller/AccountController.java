package com.example.banking.controller;

import com.example.banking.model.Transaction;
import com.example.banking.service.AccountService;

import java.util.List;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService service) {
        this.accountService = service;
    }

    public void createAccount(String accNum, String holder, double initialBalance) {
        try {
            accountService.createAccount(accNum, holder, initialBalance);
            System.out.println("Account created successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
    }

    public void deposit(String accNum, double amount) {
        try {
            accountService.deposit(accNum, amount);
            System.out.println("Deposit successful.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error during deposit: " + e.getMessage());
        }
    }

    public void withdraw(String accNum, double amount) {
        try {
            accountService.withdraw(accNum, amount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
        }
    }

    public void transfer(String fromAcc, String toAcc, double amount) {
        try {
            accountService.transfer(fromAcc, toAcc, amount);
            System.out.println("Transfer successful.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error during transfer: " + e.getMessage());
        }
    }

    public void printBalance(String accNum) {
        try {
            double balance = accountService.getBalance(accNum);
            System.out.println("Balance for account " + accNum + ": " + balance);
        } catch (IllegalArgumentException e) {
            System.err.println("Error fetching balance: " + e.getMessage());
        }
    }

    public void printTransactionHistory(String accNum) {
        try {
            List<Transaction> transactions = accountService.getTransactionHistory(accNum);
            System.out.println("Transaction history for account " + accNum + ":");
            for (Transaction t : transactions) {
                System.out.println(t.getTimestamp() + " - " + t.getType() + " - " + t.getAmount());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
    }
}
