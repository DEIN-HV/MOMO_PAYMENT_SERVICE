package com.wallet.account;

public class Account {
    private long balance;

    public Account() {
        this.balance = 0L;
    }

    public long getBalance() {
        return balance;
    }

    void setBalance(long balance) {
        this.balance = balance;
    }
}