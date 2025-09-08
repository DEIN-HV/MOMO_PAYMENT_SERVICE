package com.payment.account;

public class Account {
    private long balance;

    public Account() {
        this.balance = 0L;
    }

    public long getBalance() {
        return balance;
    }

    public void cashIn(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public boolean debit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
