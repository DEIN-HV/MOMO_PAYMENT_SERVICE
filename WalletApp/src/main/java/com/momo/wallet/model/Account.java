package com.momo.wallet.model;

public class Account {
    private long balance;

    public Account() {
        this.balance = 0L;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}