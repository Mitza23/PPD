package mihai;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final int[] items = new int[100];
    int putptr, takeptr, count;

    public void put(int x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            putptr += 1;
            count += 1;
            if(putptr == items.length) {
                putptr = 0;
            }
            if(count > 0) {
                notEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public int take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            int x = items[takeptr];
            takeptr += 1;
            if(takeptr == items.length){
                takeptr = 0;
            }
            count -= 1;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}