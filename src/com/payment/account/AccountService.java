package com.payment.account;

public class AccountService {
    private final Account account;

    public AccountService(Account account) {
        this.account = account;
    }

    public void cashIn(long amount) {
        account.cashIn(amount);
    }

    public long getBalance() {
        return account.getBalance();
    }

    public boolean debit(long amount) {
        return account.debit(amount);
    }
}
