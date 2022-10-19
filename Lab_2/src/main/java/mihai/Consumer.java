package mihai;

import java.util.LinkedList;
import java.util.Queue;

public class Consumer extends Thread{
    public int sum;
    public  BoundedBuffer buffer;

    public Consumer(BoundedBuffer buffer) {
        this.sum = 0;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sum += buffer.take();
                System.out.println("Sum: " + sum);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
