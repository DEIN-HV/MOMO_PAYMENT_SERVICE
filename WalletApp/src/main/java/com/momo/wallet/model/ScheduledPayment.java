package com.momo.wallet.model;

import java.time.LocalDate;

public class ScheduledPayment {
    private int billId;
    private LocalDate scheduledDate;
    private boolean executed;

    public ScheduledPayment(int billId, LocalDate scheduledDate) {
        this.billId = billId;
        this.scheduledDate = scheduledDate;
        this.executed = false;
    }

    public int getBillId() { return billId; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public boolean isExecuted() { return executed; }
    public void setExecuted(boolean executed) { this.executed = executed; }
}
