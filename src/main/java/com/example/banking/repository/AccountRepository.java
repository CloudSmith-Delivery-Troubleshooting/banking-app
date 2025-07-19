package com.example.banking.repository;

import com.example.banking.model.Account;
import com.example.banking.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {
    private static final String JDBC_URL = "jdbc:h2:./data/bankingdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    public AccountRepository() {
        // Initialize schema
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number VARCHAR(255) PRIMARY KEY, " +
                    "account_holder_name VARCHAR(255) NOT NULL, " +
                    "balance DOUBLE NOT NULL)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "account_number VARCHAR(255) NOT NULL, " +
                    "type VARCHAR(50) NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "timestamp TIMESTAMP NOT NULL, " +
                    "FOREIGN KEY (account_number) REFERENCES accounts(account_number)" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    public void save(Account account) {
        try (Connection conn = getConnection()) {
            // Try update first
            String updateSql = "UPDATE accounts SET account_holder_name=?, balance=? WHERE account_number=?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, account.getAccountHolderName());
                updateStmt.setDouble(2, account.getBalance());
                updateStmt.setString(3, account.getAccountNumber());
                int rows = updateStmt.executeUpdate();
                if (rows == 0) {
                    // Insert if not exists
                    String insertSql = "INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, account.getAccountNumber());
                        insertStmt.setString(2, account.getAccountHolderName());
                        insertStmt.setDouble(3, account.getBalance());
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }

    public void saveTransaction(String accountNumber, String type, double amount, LocalDateTime timestamp) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO transactions (account_number, type, amount, timestamp) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                stmt.setString(2, type);
                stmt.setDouble(3, amount);
                stmt.setTimestamp(4, Timestamp.valueOf(timestamp));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    public List<Transaction> findTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT type, amount, timestamp FROM transactions WHERE account_number = ? ORDER BY timestamp ASC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String type = rs.getString("type");
                        double amount = rs.getDouble("amount");
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        transactions.add(new Transaction(type, amount, timestamp));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load transactions", e);
        }
        return transactions;
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT account_number, account_holder_name, balance FROM accounts WHERE account_number = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, accountNumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String accNum = rs.getString("account_number");
                        String holder = rs.getString("account_holder_name");
                        double balance = rs.getDouble("balance");
                        Account account = new Account(accNum, holder, balance);
                        // Load and set transactions
                        List<Transaction> transactions = findTransactionsByAccountNumber(accNum);
                        account.setTransactions(transactions);
                        return Optional.of(account);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account", e);
        }
    }

    public void clearAll() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM transactions");
            stmt.executeUpdate("DELETE FROM accounts");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear database tables", e);
        }
    }
}
