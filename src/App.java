import com.payment.account.Account;
import com.payment.account.AccountService;
import com.payment.cli.CommandProcessor;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Account account = new Account();
        AccountService accountService = new AccountService(account);
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
