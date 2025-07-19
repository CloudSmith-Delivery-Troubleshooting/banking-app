package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        accountRepository = new AccountRepository();
        accountRepository.clearAll();
        accountService = new AccountService(accountRepository);
    }

    @Test
    void testCreateAccountAndDeposit() {
        Account acc = accountService.createAccount("ACC1", "Test User", 100);
        assertEquals(100, acc.getBalance());

        accountService.deposit("ACC1", 50);
        Account updatedAcc = accountRepository.findByAccountNumber("ACC1").get();
        assertEquals(150, updatedAcc.getBalance());
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

    @Test
    void testCreateAccountWithNullAccountNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(null, "Holder", 100);
        });
        assertEquals("Account number cannot be null or empty", e.getMessage());
    }

    @Test
    void testCreateAccountWithEmptyAccountNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount("", "Holder", 100);
        });
        assertEquals("Account number cannot be null or empty", e.getMessage());
    }

    @Test
    void testCreateAccountWithNullHolderName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount("ACC123", null, 100);
        });
        assertEquals("Account holder name cannot be null or empty", e.getMessage());
    }

    @Test
    void testCreateAccountWithEmptyHolderName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount("ACC123", "", 100);
        });
        assertEquals("Account holder name cannot be null or empty", e.getMessage());
    }

    @Test
    void testCreateAccountWithNegativeInitialBalance() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount("ACC123", "Holder", -10);
        });
        assertEquals("Initial balance cannot be negative", e.getMessage());
    }

    // Deposit negative amount
    @Test
    void testDepositNegativeAmount() {
        Account acc = accountService.createAccount("ACC1", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit("ACC1", -50);
        });
        assertEquals("Deposit amount must be positive", e.getMessage());
        assertEquals(100, acc.getBalance());
    }

    // Withdraw negative amount
    @Test
    void testWithdrawNegativeAmount() {
        Account acc = accountService.createAccount("ACC2", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw("ACC2", -30);
        });
        assertEquals("Withdrawal amount must be positive", e.getMessage());
        assertEquals(100, acc.getBalance());
    }

    // Withdraw more than balance
    @Test
    void testWithdrawMoreThanBalance() {
        Account acc = accountService.createAccount("ACC3", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw("ACC3", 150);
        });
        assertEquals("Insufficient balance", e.getMessage());
        assertEquals(100, acc.getBalance());
    }

    // Transfer to same account
    @Test
    void testTransferToSameAccount() {
        accountService.createAccount("ACC4", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("ACC4", "ACC4", 50);
        });
        assertEquals("Cannot transfer to the same account", e.getMessage());
    }

    // Transfer from non-existing account
    @Test
    void testTransferFromNonExistingAccount() {
        accountService.createAccount("ACC5", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("NON_EXIST", "ACC5", 50);
        });
        assertEquals("Source account not found", e.getMessage());
    }

    // Transfer to non-existing account
    @Test
    void testTransferToNonExistingAccount() {
        accountService.createAccount("ACC6", "User", 100);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("ACC6", "NON_EXIST", 50);
        });
        assertEquals("Destination account not found", e.getMessage());
    }

    // Deposit to non-existing account
    @Test
    void testDepositToNonExistingAccount() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit("NON_EXIST", 100);
        });
        assertEquals("Account not found", e.getMessage());
    }

    // Withdraw from non-existing account
    @Test
    void testWithdrawFromNonExistingAccount() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw("NON_EXIST", 50);
        });
        assertEquals("Account not found", e.getMessage());
    }

    // Null account number in deposit
    @Test
    void testDepositNullAccountNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(null, 100);
        });
        assertEquals("Account not found", e.getMessage());
    }

    // Null account number in withdraw
    @Test
    void testWithdrawNullAccountNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw(null, 50);
        });
        assertEquals("Account not found", e.getMessage());
    }

    // Null account number in transfer
    @Test
    void testTransferNullAccountNumber() {
        accountService.createAccount("ACC7", "User", 100);
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer(null, "ACC7", 50);
        });
        assertEquals("Source account not found", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("ACC7", null, 50);
        });
        assertEquals("Destination account not found", e2.getMessage());
    }
}
