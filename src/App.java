import java.util.Scanner;

import com.wallet.account.Account;
import com.wallet.account.AccountService;
import com.wallet.bill.BillService;
import com.wallet.cli.CommandProcessor;

public class App {
    public static void main(String[] args) {
        Account account = new Account();
        AccountService accountService = new AccountService(account);
        BillService billService = new BillService(account);
        CommandProcessor processor = new CommandProcessor(accountService);

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
