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
        amount = 10000;
        lock = new ReentrantLock();
        logs = new ArrayList<>();
    }

    public synchronized void logTransaction(Account to, int value) {
        logs.add(new Log(to, value));
    }

    public void transfer(Account from, int value) {
        Account small;
        Account big;
        if(this.id < from.id) {
            small = this;
            big = from;
        }
        else {
            small = from;
            big = this;
        }
        small.lock.lock();
        try {
            big.lock.lock();
            try {
                if (from.amount >= value) {
                    this.logTransaction(from, -value);
                    from.logTransaction(this, value);
                    from.amount -= value;
                    amount += value;
                }
            } finally {
                big.lock.unlock();
            }
        } finally {
            small.lock.unlock();
        }
//        if (from.amount >= value) {
//            this.logTransaction(from, -value);
//            from.logTransaction(this, value);
//            from.amount -= value;
//            amount += value;
//        }
    }

    public boolean verify() {
        int total = amount;
        for (var log : logs) {
            total += log.value;
        }
        return total == 10000;
    }

    @Override
    public String toString() {
        return "Account " + id;
    }
}
