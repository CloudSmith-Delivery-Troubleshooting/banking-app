package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountService = new AccountService(new AccountRepository());
    }

    @Test
    void testCreateAccountAndDeposit() {
        Account acc = accountService.createAccount("ACC1", "Test User", 100);
        assertEquals(100, acc.getBalance());

        accountService.deposit("ACC1", 50);
        assertEquals(150, acc.getBalance());
    }

    @Test
    void testWithdrawInsufficientBalance() {
        accountService.createAccount("ACC2", "Test User 2", 100);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw("ACC2", 200);
        });
        assertTrue(exception.getMessage().contains("Insufficient balance"));
    }

    @Test
    void testTransferBetweenAccounts() {
        accountService.createAccount("ACC3", "User 3", 300);
        accountService.createAccount("ACC4", "User 4", 100);

        accountService.transfer("ACC3", "ACC4", 150);

        assertEquals(150, accountService.getBalance("ACC3"));
        assertEquals(250, accountService.getBalance("ACC4"));
    }
}
