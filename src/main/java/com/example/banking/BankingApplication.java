package com.example.banking;

import com.example.banking.controller.AccountController;
import com.example.banking.repository.AccountRepository;
import com.example.banking.service.AccountService;

public class BankingApplication {
    public static void main(String[] args) {
        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);
        AccountController controller = new AccountController(accountService);

        // Sample usage
        controller.createAccount("ACC1001", "Alice", 1000);
        controller.createAccount("ACC1002", "Bob", 500);

        controller.deposit("ACC1001", 200);
        controller.withdraw("ACC1002", 100);

        controller.transfer("ACC1001", "ACC1002", 300);

        controller.printBalance("ACC1001");
        controller.printBalance("ACC1002");

        controller.printTransactionHistory("ACC1001");
        controller.printTransactionHistory("ACC1002");

        // Example failure scenario:
        controller.withdraw("ACC1002", 2000); // Should print error: Insufficient balance
    }
}
