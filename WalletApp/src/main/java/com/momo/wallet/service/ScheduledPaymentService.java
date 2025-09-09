package com.momo.wallet.service;

import java.time.LocalDate;

public interface ScheduledPaymentService {
    void schedulePayment(int billId, LocalDate date);
    void processScheduledPayments();
}