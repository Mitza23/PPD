import java.util.ArrayList;
import java.util.List;

class Bank {
    public List<Account> accounts;
    public List<Thread> threads;

    public Thread checker;

    public int accountCount = 1000;
    public int threadCount = 20;

    public Bank() {
        accounts = new ArrayList<>();
        threads = new ArrayList<>();
        for (int i = 0; i < accountCount; i++) {
            accounts.add(new Account());
        }
        for (int i = 0; i < threadCount; i++) {
            threads.add(new Worker(accountCount, accounts));
        }
        checker = new Checker(accountCount, accounts);
    }

    public void work() {
        threads.forEach(Thread::start);
        checker.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        checker.interrupt();

    }

    public void verify() {
        int total = 0;
        for (var a : accounts) {
            System.out.println(a);
            total += a.amount;
            for (var log : a.logs) {
                System.out.println("\t" + a + " -> " + log);
            }
            System.out.println(a.verify());
        }
        if(total == accountCount * 10000) {
            System.out.println("No money lost");
        }
    }
}
