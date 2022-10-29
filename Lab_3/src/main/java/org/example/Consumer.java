package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer extends Thread {
    int[][] a;
    int[][] b;
    int[][] c;
    int m;
    int k;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;
    Lock lock;
    int elements;
    Boolean run;
    Condition condition;

    public Consumer(int[][] a, int[][] b, int[][] c, int m, int k, int n, BoundedBuffer<Pair<Integer, Integer>> buffer,
                    Lock lock, int elements, Boolean run, Condition condition) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.m = m;
        this.k = k;
        this.n = n;
        this.buffer = buffer;
        this.lock = lock;
        this.elements = elements;
        this.run = run;
        this.condition = condition;
    }

    public void compute() {
        try {
            var pair = buffer.take();
            int line = pair.key;
            int column = pair.value;
            int sum = 0;
            for (int i = 0; i < k; i++) {
                sum += a[line][i] * b[i][column];
            }
            lock.lock();
            c[line][column] = sum;
            elements -= 1;
            if(elements == 0) {
                run = false;
                condition.signalAll();
            }
            lock.unlock();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if(run) {
                lock.unlock();
                compute();
            }
            else {
                lock.unlock();
                return;
            }
        }
    }
}
