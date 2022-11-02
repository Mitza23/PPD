package org.example;

public class AlternativeProducer extends Thread{
    int m;
    int n;
    BoundedBuffer<Pair<Integer, Integer>> buffer;
    int elementCount;
    int order;
    int k;
    int index;

    public AlternativeProducer(int m, int n, BoundedBuffer<Pair<Integer, Integer>> buffer, int elementCount, int order,
                               int k, int index) {
        this.m = m;
        this.n = n;
        this.buffer = buffer;
        this.elementCount = elementCount;
        this.order = order;
        this.k = k;
        this.index = index;
    }

    public void generate() {
        try {
            int count = 0;
            for(int i = 0 ; i < m ; i ++) {
                for(int j = 0 ; j < n ; j ++) {
                    if(count % k == index) {
                        buffer.put(new Pair<>(i, j));
                    }
                    count += 1;
                }
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
