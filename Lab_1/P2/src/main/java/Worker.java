import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class Worker extends Thread {
    public int accountCount;
    public List<Account> accounts;

    public Worker(int accountCount, List<Account> accounts) {
        this.accountCount = accountCount;
        this.accounts = accounts;
    }

    public void work() {
        for (int i = 0; i < 10; i++) {
            int from = ThreadLocalRandom.current().nextInt(0, accountCount);
            int to = ThreadLocalRandom.current().nextInt(0, accountCount);
            int value = ThreadLocalRandom.current().nextInt(10, 300);
            accounts.get(to).transfer(accounts.get(from), value);
        }
    }

    @Override
    public void run() {
        work();
    }
}
