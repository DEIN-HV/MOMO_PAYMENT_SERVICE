// PaymentServiceImpl.java
package com.momo.wallet.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.momo.wallet.model.Bill;
import com.momo.wallet.model.Payment;
import com.momo.wallet.service.AccountService;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {
    private final AccountService accountService;
    private final BillService billService;
    private final List<Payment> payments;

    public PaymentServiceImpl(AccountService accountService, BillService billService) {
        this.accountService = accountService;
        this.billService = billService;
        this.payments = new ArrayList<>();
    }

    @Override
    public void payBills(int... billIds) {
        List<Bill> toPay = new ArrayList<>();
        for (int id : billIds) {
            Optional<Bill> billOpt = billService.listBills().stream()
                    .filter(b -> b.getId() == id)
                    .findFirst();
            if (billOpt.isEmpty()) {
                System.out.println("Sorry! Not found a bill with such id: " + id);
                return;
            }
            Bill bill = billOpt.get();
            if (bill.getPaid()) {
                System.out.println("Bill already paid: " + id);
                return;
            }
            toPay.add(bill);
        }

        long totalAmount = toPay.stream().mapToLong(Bill::getAmount).sum();
        if (accountService.getBalance() < totalAmount) {
            System.out.println("Sorry! Not enough fund to proceed with payment.");
            return;
        }

        for (Bill bill : toPay) {
            accountService.debit(bill.getAmount());
            bill.setPaid(true);
            Payment payment = new Payment(bill.getAmount(), LocalDate.now(), bill.getId(),
                    Payment.PaymentState.PROCESSED);
            payments.add(payment);
            System.out.println("Payment has been completed for Bill with id " + bill.getId());
        }
        System.out.println("Your current balance is: " + accountService.getBalance());
    }

    @Override
    public void recordPayment(Payment payment) {
        payments.add(payment);
    }

    @Override
    public void listPayments() {
        if (payments.isEmpty()) {
            System.out.println("No payment records.");
            return;
        }
        System.out.println("No.  Amount   Payment Date  State      Bill Id");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int i = 1;
        for (Payment p : payments) {
            System.out.printf("%-4d %-8d %-12s %-10s %-6d%n",
                    i++,
                    p.getAmount(),
                    p.getPaymentDate().format(formatter),
                    p.getState(),
                    p.getBillId());
        }
    }
}
