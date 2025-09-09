package com.momo.wallet.service;

import com.momo.wallet.model.Payment;

public interface PaymentService {
    void payBills(int... billIds);
    void recordPayment(Payment payment);
    void listPayments();
}