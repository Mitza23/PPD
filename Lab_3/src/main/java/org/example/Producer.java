package org.example;


public class Producer extends Thread{
    int[][] a;
    int[][] b;
    int m;
    int k;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;

    public Producer(int m, int k, int n, BoundedBuffer<Pair<Integer, Integer>> buffer) {
        this.m = m;
        this.k = k;
        this.n = n;
        this.buffer = buffer;
        a =  new int[m][k];
        b = new int[k][n];
        for(int i = 0 ; i < m ; i++) {
            for(int j = 0 ; j < k ; j++) {
                a[i][j] = 1;
            }
        }
        for(int i = 0 ; i < k ; i++) {
            for(int j = 0 ; j < n ; j++) {
                b[i][j] = 1;
            }
        }
    }

    @Override
    public void run() {
        for(int i = 0 ; i < m ; i++) {
            for(int j = 0 ; j < n ; j++) {
                try {
                    buffer.put(new Pair<>(i, j));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
