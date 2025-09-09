import java.util.Scanner;

import com.wallet.account.AccountService;
import com.wallet.bill.BillService;
import com.wallet.cli.CommandProcessor;
import com.wallet.payment.PaymentService;
import com.wallet.scheduledPayment.ScheduledPaymentService;

public class App {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        BillService billService = new BillService();
        PaymentService paymentService = new PaymentService(accountService, billService);
        ScheduledPaymentService scheduledPaymentService = new ScheduledPaymentService(paymentService, billService);
        CommandProcessor processor = new CommandProcessor(accountService, billService, paymentService, scheduledPaymentService);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (!processor.process(line)) {
                break;
            }
        }
        scanner.close();
    }
}
