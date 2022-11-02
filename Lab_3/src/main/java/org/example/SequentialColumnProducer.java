package org.example;

public class SequentialColumnProducer extends Thread{
    int m;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;
    int elementCount;
    int order;

    public SequentialColumnProducer(int m, int n, BoundedBuffer<Pair<Integer, Integer>> buffer,
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
            int rowsComputed = computedElementCount % m;
            int columnsComputed = computedElementCount / m;
            for (int i = rowsComputed; i < m; i++) {
                buffer.put(new Pair<>(i, columnsComputed));
                elementCount -= 1;

            }
            columnsComputed += 1;
            int rowsToCompute = elementCount % m;
            int columnsToCompute = elementCount / m;
            for (int j = 0; j < columnsToCompute; j++) {
                for (int i = 0; i < m; i++) {
                    buffer.put(new Pair<>(i, columnsComputed + j));
                }
            }
            columnsComputed += columnsToCompute;
            for (int i = 0; i < rowsToCompute; i++) {
                buffer.put(new Pair<>(i, columnsComputed));
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
