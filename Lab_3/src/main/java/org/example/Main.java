package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        int m = 100;
        int k = 100;
        int n = 100;
        int[][] a = new int[m][k];
        int[][] b = new int[k][n];
        int[][] c = new int[m][n];
        int elements = n * m;
        Boolean run = true;
        BoundedBuffer<Pair<Integer, Integer>> buffer = new BoundedBuffer<>(10);
        Lock lock = new ReentrantLock();
        Condition done = lock.newCondition();
        Producer producer = new Producer(a, b, m, k, n, buffer);
        List<Thread> consumers = new ArrayList<>();
        int consumerCount = 10;
        for(int i = 0 ; i < consumerCount ; i++) {
            var cns = new Consumer(a, b, c, m, k, n, buffer, lock, elements, run, done);
            consumers.add(cns);
            cns.start();
        }
        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int s = 0;
        for(int i = 0 ; i < m ; i ++) {
            for(int j = 0 ; j < n ; j++) {
                s += c[i][j];
            }
        }
        System.out.println(s == n*m);
    }
}