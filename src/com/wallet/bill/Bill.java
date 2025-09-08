package com.wallet.bill;

import java.time.LocalDate;

public class Bill {
    private final int id;
    private String type; // ELECTRIC, WATER, INTERNET, ...
    private long amount;
    private LocalDate dueDate;
    private String provider; // EVN HCMC, VNPT, ...
    private boolean paid;

    public Bill(int id, String type, long amount, LocalDate dueDate, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.paid = false;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getProvider() {
        return provider;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void markAsPaid() {
        this.paid = true;
    }

    @Override
    public String toString() {
        return id + ". " + type + " " + amount + " " + dueDate + " " + (paid ? "PAID" : "NOT_PAID") + " " + provider;
    }
}