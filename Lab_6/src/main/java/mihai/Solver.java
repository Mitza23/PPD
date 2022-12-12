package mihai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Solver {
    private int[][] M;
    private int vertexCount;
    private ExecutorService executorService;

    public void generateGraph(int vertexCount, int edgeCount) {
        this.vertexCount = vertexCount;
        M = new int[vertexCount][vertexCount];
        while (edgeCount > 0) {
            int src, dest;
            do {
                src = ThreadLocalRandom.current().nextInt(0, vertexCount);
                dest = ThreadLocalRandom.current().nextInt(0, vertexCount);
            } while (M[src][dest] != 0);
            M[src][dest] = 1;
            edgeCount -= 1;
        }
        for (int i = -1; i < vertexCount; i++) {
            System.out.printf("" + i + " ");
        }
        System.out.println("");
        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("" + i + ": ");
            for (int j = 0; j < vertexCount; j++) {
                System.out.printf("" + M[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void searchHamiltonianCycle(int src) {
        int count = 0;
        for (int j = 0; j < vertexCount; j++) {
            if (M[src][j] == 1) {
                count += 1;
            }
        }
        executorService = Executors.newFixedThreadPool(count);
        List<List<Integer>> paths = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(src);
            paths.add(list);
        }
        int pos = 0;
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int j = 0; j < vertexCount; j++) {
            if (M[src][j] == 1) {
                var path = paths.get(pos);
                path.add(j);
                futures.add(executorService.submit(new SearchCallable(j, path, vertexCount, M, src)));
                pos += 1;
            }
        }
        executorService.shutdown();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < count; i++) {
//            System.out.println(paths.get(i));
            try {
                if (futures.get(i).get()) {
                    System.out.println("Hamiltonian cycle: ");
                    System.out.println(paths.get(i));
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
