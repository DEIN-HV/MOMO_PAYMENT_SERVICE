package com.wallet.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.wallet.account.AccountService;
import com.wallet.bill.Bill;
import com.wallet.bill.BillService;
import com.wallet.payment.PaymentService;

public class CommandProcessor {
    private final AccountService accountService;
    private final BillService billService;
    private final PaymentService paymentService;

    public CommandProcessor(AccountService accountService, BillService billService, PaymentService paymentService) {
        this.accountService = accountService;
        this.billService = billService;
        this.paymentService = paymentService;
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

            default:
                System.out.println("Unknown command: " + command);
        }
        return true;
    }
}
