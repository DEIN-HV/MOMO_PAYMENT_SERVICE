package com.payment.account;

public class Account {
    private long balance;

    public Account() {
        this.balance = 0L;
    }

    public long getBalance() {
        return balance;
    }

    // package-private (chỉ service gọi được)
    void setBalance(long balance) {
        this.balance = balance;
    }
}