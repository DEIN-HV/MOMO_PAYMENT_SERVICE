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
import com.momo.wallet.service.impl.AccountServiceImpl;
import com.momo.wallet.service.impl.BillServiceImpl;
import com.momo.wallet.service.impl.PaymentServiceImpl;
import com.momo.wallet.service.impl.ScheduledPaymentServiceImpl;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        AccountService accountService = new AccountServiceImpl();
        BillService billService = new BillServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl(accountService, billService);
        ScheduledPaymentService scheduledPaymentService = new ScheduledPaymentServiceImpl(paymentService, billService);
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