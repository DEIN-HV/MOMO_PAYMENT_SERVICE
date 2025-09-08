package com.wallet.cli;

import java.time.LocalDate;

import com.wallet.account.AccountService;
import com.wallet.bill.Bill;
import com.wallet.bill.BillService;

public class CommandProcessor {
    private final AccountService accountService;

    private final BillService billService;

    public CommandProcessor(AccountService accountService, BillService billService) {
        this.accountService = accountService;
        this.billService = billService;
    }

    public boolean process(String line) {
        String[] tokens = line.split(" ");
        String command = tokens[0];

        // STOP PROGRAM
        if ("EXIT".equalsIgnoreCase(command)) {
            System.out.println("Good bye!");
            return false; 
        }

        switch (command.toUpperCase()) {
            // FEATURE 1: ACCOUNT MANAGEMENT - CASH IN
            case "CASH_IN":
                if (tokens.length < 2) {
                    System.out.println("Usage: CASH_IN <amount>");
                    break;
                }
                long amount = Long.parseLong(tokens[1]);
                accountService.cashIn(amount);
                System.out.println("Your available balance: " + accountService.getBalance());
                break;

            case "LIST_BILL":
                for (Bill b : billService.listBills()) {
                    System.out.println(b);
                }
                break;

            case "CREATE_BILL":
                if (tokens.length < 5) {
                    System.out.println("Usage: CREATE_BILL <type> <amount> <dueDate:yyyy-MM-dd> <provider>");
                    break;
                }
                String type = tokens[1];
                long amount2 = Long.parseLong(tokens[2]);
                LocalDate dueDate = LocalDate.parse(tokens[3]);
                String provider = tokens[4];
                Bill newBill = billService.createBill(type, amount2, dueDate, provider);
                System.out.println("Created bill: " + newBill);
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
        return true; 
    }
}
