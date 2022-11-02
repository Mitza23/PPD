package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer<T> {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    int size;
    List<T> items;
    int putptr, takeptr, count;

    public BoundedBuffer(int size) {
        this.size = size;
        this.items = new ArrayList<>();
        for(int i = 0 ; i < size ; i++) {
            items.add(null);
        }
    }

    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == size)
                notFull.await();
            items.set(putptr, x);
            putptr += 1;
            count += 1;
            if(putptr == size) {
                putptr = 0;
            }
            if(count > 0) {
                notEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            T x = items.get(takeptr);
            takeptr += 1;
            if(takeptr == size){
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