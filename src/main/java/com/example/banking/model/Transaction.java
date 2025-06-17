package com.example.banking.model;

import java.time.LocalDateTime;

public class Transaction {
    private final String type;
    private final double amount;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
