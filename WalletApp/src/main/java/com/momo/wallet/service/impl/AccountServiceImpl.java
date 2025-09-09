// AccountServiceImpl.java
package com.momo.wallet.service.impl;

import com.momo.wallet.model.Account;
import com.momo.wallet.service.AccountService;

public class AccountServiceImpl implements AccountService {
    private Account account = new Account();

    @Override
    public void cashIn(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        long newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        System.out.println("Cash in successful. New balance: " + newBalance);
    }

    @Override
    public boolean debit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (account.getBalance() >= amount) {
            long newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            System.out.println("Debit successful. New balance: " + newBalance);
            return true;
        } else {
            System.out.println("Debit failed. Insufficient balance.");
            return false;
        }
    }

    @Override
    public long getBalance() {
        return account.getBalance();
    }
}
