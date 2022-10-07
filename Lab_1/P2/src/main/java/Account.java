import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
                if (from.amount >= value) {
                    this.logTransaction(from, -value);
                    from.logTransaction(this, value);
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
