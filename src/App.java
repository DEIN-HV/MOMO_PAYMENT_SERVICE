import com.payment.account.Account;
import com.payment.account.AccountService;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        AccountService accountService = new AccountService(new Account());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("EXIT")) {
                System.out.println("Good bye!");
                break;
            }

            String[] tokens = line.split(" ");
            String command = tokens[0];

            if ("CASH_IN".equalsIgnoreCase(command)) {
                if (tokens.length < 2) {
                    System.out.println("Usage: CASH_IN <amount>");
                    continue;
                }
                long amount = Long.parseLong(tokens[1]);
                accountService.cashIn(amount);
                System.out.println("Your available balance: " + accountService.getBalance());
            } else {
                System.out.println("Unknown command");
            }
        }
    }
}
