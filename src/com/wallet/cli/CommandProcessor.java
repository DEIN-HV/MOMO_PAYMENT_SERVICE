package com.payment.cli;

import com.payment.account.AccountService;

public class CommandProcessor {
    private final AccountService accountService;

    public CommandProcessor(AccountService accountService) {
        this.accountService = accountService;
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

            

            default:
                System.out.println("Unknown command: " + command);
        }
        return true; 
    }
}
