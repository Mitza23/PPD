import java.util.List;

public class Checker extends Thread{
    public int accountCount;
    public List<Account> accounts;

    public Checker(int accountCount, List<Account> accounts) {
        this.accountCount = accountCount;
        this.accounts = accounts;
    }

    public boolean verify() {
        for(var acc: accounts) {
            acc.lock.lock();
        }

        boolean ok = true;
        for(var acc: accounts) {
            ok = ok & acc.verify();
        }

        for(var acc: accounts) {
            acc.lock.unlock();
        }
        System.out.println("Checking:....." + ok);
        return ok;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            verify();
        }
    }
}
