package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int m = 100;
        int k = 100;
        int n = 100;
        int[][] a = new int[m][k];
        int[][] b = new int[k][n];
        int[][] c = new int[m][n];
        for(int i = 0 ; i < m ; i ++) {
            for(int j = 0 ; j < k ; j++) {
                a[i][j] = 1;
            }
        }
        for(int i = 0 ; i < k ; i ++) {
            for(int j = 0 ; j < n ; j++) {
                b[i][j] = 1;
            }
        }

        int s = 0;
        for(int i = 0 ; i < m ; i ++) {
            for(int j = 0 ; j < n ; j++) {
                s += c[i][j];
            }
        }
    }

    public void sequentialRow(int m, int n) {
        int threadCount = 5;
        List<BoundedBuffer<Pair<Integer, Integer>>> buffers = new ArrayList<>();
        int elementCount = m * n / threadCount;
        for(int i = 0 ; i < threadCount ; i ++) {
            var b = new BoundedBuffer<Pair<Integer, Integer>>(10);
            buffers.add(b);
            SequentialRowProducer producer = new SequentialRowProducer(m, n, b, elementCount, i);
        }
        for(int i = 0 ; i < threadCount ; i ++) {

            SequentialRowProducer producer = new SequentialRowProducer(m, n, b, elementCount, i);
        }


    }

    public void sequentialColumn() {

    }

    public void alternative() {

    }
}