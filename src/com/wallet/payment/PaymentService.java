package com.wallet.payment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.wallet.account.Account;
import com.wallet.account.AccountService;
import com.wallet.bill.Bill;

public class PaymentService {
    private final Account account;
    private final AccountService accountService;
    private final List<Bill> bills;
    private final List<Payment> payments;

    public PaymentService(Account account, AccountService accountService, List<Bill> bills) {
        this.account = account;
        this.accountService = accountService;
        this.bills = bills;
        this.payments = new ArrayList<>();
    }

    public void payBills(int... billIds) {
        // Sắp xếp theo ngày đến hạn nếu muốn ưu tiên thanh toán
        List<Bill> toPay = new ArrayList<>();
        for (int id : billIds) {
            Optional<Bill> billOpt = bills.stream()
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
        if (account.getBalance() < totalAmount) {
            System.out.println("Sorry! Not enough fund to proceed with payment.");
            return;
        }

        // Thanh toán từng bill
        for (Bill bill : toPay) {
            accountService.debit(bill.getAmount());
            bill.setPaid(true);
            Payment payment = new Payment(bill.getAmount(), LocalDate.now(), bill.getId(),
                    Payment.PaymentState.PROCESSED);
            payments.add(payment);
            System.out.println("Payment has been completed for Bill with id " + bill.getId());
        }
        System.out.println("Your current balance is: " + account.getBalance());
    }

    public List<Payment> listPayments() {
        return payments;
    }
}
