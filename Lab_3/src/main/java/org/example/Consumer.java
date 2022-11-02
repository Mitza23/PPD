package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Consumer extends Thread {
    int[][] a;
    int[][] b;
    int[][] c;
    int m;
    int k;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;
    int elements;
    CyclicBarrier barrier;

    public Consumer(int[][] a, int[][] b, int[][] c, int m, int k, int n, BoundedBuffer<Pair<Integer, Integer>> buffer,
                    int elements, CyclicBarrier barrier) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.m = m;
        this.k = k;
        this.n = n;
        this.buffer = buffer;
        this.elements = elements;
        this.barrier = barrier;
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
            c[line][column] = sum;
            elements -= 1;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (elements > 0) {
            compute();
        }
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
