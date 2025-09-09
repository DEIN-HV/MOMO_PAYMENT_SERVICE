package com.momo.wallet.service;

public interface AccountService {
    void cashIn(long amount);
    boolean debit(long amount);
    long getBalance();
}