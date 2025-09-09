package com.momo.wallet.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.momo.wallet.model.Bill;
import com.momo.wallet.service.AccountService;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.PaymentService;
import com.momo.wallet.service.ScheduledPaymentService;


public class CommandProcessor {
    private final AccountService accountService;
    private final BillService billService;
    private final PaymentService paymentService;
    private final ScheduledPaymentService scheduledPaymentService;

    public CommandProcessor(AccountService accountService, BillService billService, PaymentService paymentService, ScheduledPaymentService scheduledPaymentService) {
        this.accountService = accountService;
        this.billService = billService;
        this.paymentService = paymentService;
        this.scheduledPaymentService = scheduledPaymentService;
    }

    public boolean process(String line) {
        String[] inputArgs = line.split(" ");
        String command = inputArgs[0];

        // STOP PROGRAM
        if ("EXIT".equalsIgnoreCase(command)) {
            System.out.println("Good bye!");
            return false;
        }

        switch (command.toUpperCase()) {
            // FEATURE 1: ACCOUNT MANAGEMENT - CASH IN
            case "CASH_IN":
                if (inputArgs.length < 2) {
                    System.out.println("Usage: CASH_IN <amount>");
                    break;
                }
                long amount = Long.parseLong(inputArgs[1]);
                accountService.cashIn(amount);
                System.out.println("Your available balance: " + accountService.getBalance());
                break;

            case "CREATE_BILL":
                String type = inputArgs[1];
                long billAmount = Long.parseLong(inputArgs[2]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dueDate = LocalDate.parse(inputArgs[3], formatter); // format YYYY-MM-DD
                String provider = inputArgs[4];
                Bill bill = billService.createBill(type, billAmount, dueDate, provider);
                System.out.println("Bill created: " + bill);
                break;

            case "LIST_BILL":
                List<Bill> bills = billService.listBills();
                if (bills.isEmpty()) {
                    System.out.println("No bills available.");
                } else {
                    for (Bill b : bills) {
                        System.out.println(b);
                    }
                }
                break;

            case "VIEW_BILL":
                int id = Integer.parseInt(inputArgs[1]);
                Bill b = billService.viewBill(id);
                System.out.println(b != null ? b : "Bill not found");
                break;

            case "DELETE_BILL":
                int deleteId = Integer.parseInt(inputArgs[1]);
                boolean deleted = billService.deleteBill(deleteId);
                System.out.println(deleted ? "Bill deleted." : "Bill not found.");
                break;

            case "SEARCH_BILL_BY_PROVIDER":
                String prov = inputArgs[1];
                List<Bill> result = billService.searchByProvider(prov);
                if (result.isEmpty()) {
                    System.out.println("No bills found for provider " + prov);
                } else {
                    for (Bill rb : result) {
                        System.out.println(rb);
                    }
                }
                break;

            case "PAY":
                if (inputArgs.length < 2) {
                    System.out.println("Usage: PAY <billId> [<billId>...]");
                    break;
                }
                int[] billIds = new int[inputArgs.length - 1];
                for (int i = 1; i < inputArgs.length; i++) {
                    billIds[i - 1] = Integer.parseInt(inputArgs[i]);
                }
                paymentService.payBills(billIds);
                break;
            
            case "SCHEDULE":
                if (inputArgs.length < 3) {
                    System.out.println("Usage: SCHEDULE <billId> <dd/MM/yyyy>");
                    break;
                }
                int billId = Integer.parseInt(inputArgs[1]);
                LocalDate date = LocalDate.parse(inputArgs[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                scheduledPaymentService.schedulePayment(billId, date);
                break;

            case "DUE_DATE":
                List<Bill> dueBills = billService.listDueBills();
                if (dueBills.isEmpty()) {
                    System.out.println("No due bills.");
                    break;
                }
                System.out.println("Bill No.  Type      Amount   Due Date    State     PROVIDER");
                for (Bill bi : dueBills) {
                    System.out.printf("%-10d %-10s %-8d %-12s %-10s %-15s%n",
                            bi.getId(),
                            bi.getType(),
                            bi.getAmount(),
                            bi.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            bi.getPaid() ? "PAID" : "NOT_PAID",
                            bi.getProvider());
                }

            case "LIST_PAYMENT":
                paymentService.listPayments();
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
        return true;
    }
}
