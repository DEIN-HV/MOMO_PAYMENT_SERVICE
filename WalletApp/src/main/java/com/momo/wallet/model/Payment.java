package com.momo.wallet.model;

import java.time.LocalDate;

public class Payment {
    private long amount;
    private LocalDate paymentDate;
    private int billId;
    private PaymentState state;

    public enum PaymentState {
        PENDING,
        PROCESSED
    }

    public Payment(long amount, LocalDate paymentDate, int billId, PaymentState state) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.billId = billId;
        this.state = state;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public int getBillId() {
        return billId;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("Bill Id: %d, Amount: %d, Date: %s, State: %s",
                billId, amount, paymentDate, state);
    }
}
