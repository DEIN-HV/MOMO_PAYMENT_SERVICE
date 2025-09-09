package com.momo.wallet.service;
import com.momo.wallet.model.Account;

public class AccountService {
    Account account = new Account();

    // Cash in
    public void cashIn(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        long newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        System.out.println("Cash in successful. New balance: " + newBalance);
    }

    // Debit
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

    // View balance
    public long getBalance() {
        return account.getBalance();
    }
}
