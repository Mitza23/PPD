package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void sequentialRow(int m, int k, int n, int a[][], int b[][], int c[][], CyclicBarrier barrier,
                                     int threadCount) {
        List<BoundedBuffer<Pair<Integer, Integer>>> buffers = new ArrayList<>();
        int elementCount = m * n / threadCount;
        for (int i = 0; i < threadCount; i++) {
            var buffer = new BoundedBuffer<Pair<Integer, Integer>>(10);
            buffers.add(buffer);
            SequentialRowProducer producer = new SequentialRowProducer(m, n, buffer, elementCount, i);
            producer.start();
        }
        for (int i = 0; i < threadCount; i++) {
            Consumer consumer = new Consumer(a, b, c, m, k, n, buffers.get(i), elementCount, barrier);
            consumer.start();
        }
    }

    public static void sequentialColumn(int m, int k, int n, int a[][], int b[][], int c[][], CyclicBarrier barrier,
                                        int threadCount) {
        List<BoundedBuffer<Pair<Integer, Integer>>> buffers = new ArrayList<>();
        int elementCount = m * n / threadCount;
        for (int i = 0; i < threadCount; i++) {
            var buffer = new BoundedBuffer<Pair<Integer, Integer>>(10);
            buffers.add(buffer);
            SequentialColumnProducer producer = new SequentialColumnProducer(m, n, buffer, elementCount, i);
            producer.start();
        }
        for (int i = 0; i < threadCount; i++) {
            Consumer consumer = new Consumer(a, b, c, m, k, n, buffers.get(i), elementCount, barrier);
            consumer.start();
        }
    }

    public static void alternative(int m, int k, int n, int a[][], int b[][], int c[][], CyclicBarrier barrier,
                                   int threadCount) {
        List<BoundedBuffer<Pair<Integer, Integer>>> buffers = new ArrayList<>();
        int elementCount = m * n / threadCount;
        for (int i = 0; i < threadCount; i++) {
            var buffer = new BoundedBuffer<Pair<Integer, Integer>>(10);
            buffers.add(buffer);
            AlternativeProducer producer = new AlternativeProducer(m, n, buffer, elementCount, i, threadCount);
            producer.start();
        }
        for (int i = 0; i < threadCount; i++) {
            Consumer consumer = new Consumer(a, b, c, m, k, n, buffers.get(i), elementCount, barrier);
            consumer.start();
        }
    }

    public static void main(String[] args) {
        int m = 100;
        int k = 100;
        int n = 100;
        int[][] a = new int[m][k];
        int[][] b = new int[k][n];
        int[][] c = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                a[i][j] = 1;
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = 1;
            }
        }

        int threadCount = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadCount + 1);
        alternative(m, k, n, a, b, c, barrier, threadCount);
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        int s = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                s += c[i][j];
            }
        }
        System.out.println(n * m);
        System.out.println(s);
        System.out.println("Sum is: " + s + " " + (s == n * m * k));
    }
}