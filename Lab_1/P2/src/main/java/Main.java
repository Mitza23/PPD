import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.work();
        bank.verify();
    }
}

class Log {
    public Account to;
    public int value;

    public Log(Account to, int value) {
        this.to = to;
        this.value = value;
    }

    @Override
    public String toString() {
        return to + " : " + value;
    }
}

class Account {
    public static int count = 0;
    public int id;
    public int amount;
    public Lock lock;
    public List<Log> logs;

    public Account() {
        id = count;
        count += 1;
        amount = 1000;
        lock = new ReentrantLock();
        logs = new ArrayList<>();
    }

    public synchronized void logTransaction(Account to, int value) {
        logs.add(new Log(to, value));
    }

    public void transfer(Account from, int value) {
        this.lock.lock();
        try {
            from.lock.lock();
            try {
                if (from.amount > value) {
                    this.logTransaction(from, value);
                    from.logTransaction(this, -value);
                    from.amount -= value;
                    amount += value;
                }
            } finally {
                from.lock.unlock();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public boolean verify() {
        int total = amount;
        for (var log : logs) {
            total += log.value;
        }
        return total == 1000;
    }

    @Override
    public String toString() {
        return "Account " + id;
    }
}

class Bank {
    public List<Account> accounts;
    public List<Thread> threads;

    public int accountCount = 100;
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
    }

    public void work() {
        threads.forEach(Thread::start);
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

    }

    public void verify() {
        for (var a : accounts) {
            for (var log : a.logs) {
                System.out.println("\t" + a + " -> " + log);
            }
            System.out.println(a.verify());
        }
    }
}

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
