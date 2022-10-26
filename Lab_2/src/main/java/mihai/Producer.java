package mihai;

public class Producer extends Thread{
    public int v1[];
    public int v2[];
    public int size;
    public BoundedBuffer buffer;
    public int index;

    public Producer(BoundedBuffer buffer) {
        this.buffer = buffer;
        index = 0;
        size = 100;
        v1 = new int[size];
        v2 = new int[size];
        for(int i = 0 ; i < size ; i++) {
            v1[i] = i + 1;
            v2[i] = 1;
        }
    }

    public int getNumber() {
        return v1[index] * v2[index++];
    }

    @Override
    public void run() {
        while(index < size) {
            try {
                buffer.put(getNumber());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
