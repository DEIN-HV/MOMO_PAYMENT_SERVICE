package com.momo.wallet.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.momo.wallet.model.Bill;
import com.momo.wallet.model.ScheduledPayment;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.PaymentService;
import com.momo.wallet.service.ScheduledPaymentService;

public class ScheduledPaymentServiceImpl implements ScheduledPaymentService {
    private final PaymentService paymentService;
    private final BillService billService;
    private final List<ScheduledPayment> scheduledPayments;

    public ScheduledPaymentServiceImpl(PaymentService paymentService, BillService billService) {
        this.paymentService = paymentService;
        this.billService = billService;
        this.scheduledPayments = new ArrayList<>();
    }

    @Override
    public void schedulePayment(int billId, LocalDate date) {
        Bill bill = billService.viewBill(billId);
        if (bill == null) {
            System.out.println("Bill not found: " + billId);
            return;
        }
        if (bill.getPaid()) {
            System.out.println("Bill already paid: " + billId);
            return;
        }
        ScheduledPayment sp = new ScheduledPayment(billId, date);
        scheduledPayments.add(sp);
        System.out.printf("Payment for bill id %d is scheduled on %s%n", 
                billId, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @Override
    public void processScheduledPayments() {
        LocalDate today = LocalDate.now();
        for (ScheduledPayment sp : scheduledPayments) {
            if (!sp.isExecuted() && !sp.getScheduledDate().isAfter(today)) {
                paymentService.payBills(sp.getBillId());
                sp.setExecuted(true);
            }
        }
    }
}
