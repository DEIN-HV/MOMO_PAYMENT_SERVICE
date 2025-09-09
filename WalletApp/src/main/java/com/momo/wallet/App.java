package com.momo.wallet;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.momo.wallet.cli.CommandProcessor;
import com.momo.wallet.service.AccountService;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.PaymentService;
import com.momo.wallet.service.ScheduledPaymentService;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        BillService billService = new BillService();
        PaymentService paymentService = new PaymentService(accountService, billService);
        ScheduledPaymentService scheduledPaymentService = new ScheduledPaymentService(paymentService, billService);
        CommandProcessor processor = new CommandProcessor(accountService, billService, paymentService, scheduledPaymentService);

        // Schedule a task to process scheduled payments every hour
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            scheduledPaymentService.processScheduledPayments();
        }, 0, 1, TimeUnit.HOURS);

        Scanner scanner = new Scanner(System.in);
        while (true) {

            // Check and process scheduled payments every time showing the prompt
            scheduledPaymentService.processScheduledPayments();

            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (!processor.process(line)) {
                break;
            }
        }
        scanner.close();
    }
}