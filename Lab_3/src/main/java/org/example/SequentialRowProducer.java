package org.example;


public class SequentialRowProducer extends Thread {
    int[][] a;
    int[][] b;
    int m;
    int k;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;
    int elementCount;
    int order;

    public SequentialRowProducer(int m, int n, BoundedBuffer<Pair<Integer, Integer>> buffer,
                                 int elementCount, int order) {
        this.m = m;
        this.n = n;
        this.buffer = buffer;
        this.elementCount = elementCount;
        this.order = order;
    }

    public void generate() {
        try {
            int computedElementCount = elementCount * order;
            int rowsComputed = computedElementCount / n;
            int columnsComputed = computedElementCount % n;
            for (int j = columnsComputed; j < n; j++) {
                buffer.put(new Pair<>(rowsComputed, j));
                elementCount -= 1;

            }
            rowsComputed += 1;
            int rowsToCompute = elementCount / n;
            int columnsToCompute = elementCount % n;
            for (int i = 0; i < rowsToCompute; i++) {
                for (int j = 0; j < n; j++) {
                    buffer.put(new Pair<>(rowsComputed + i, j));
                }
            }
            rowsComputed += rowsToCompute;
            for (int j = 0; j < columnsToCompute; j++) {
                buffer.put(new Pair<>(rowsComputed, j));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        generate();
    }
}
