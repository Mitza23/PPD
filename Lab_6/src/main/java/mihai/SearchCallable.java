package mihai;

import java.util.List;
import java.util.concurrent.Callable;

public class SearchCallable implements Callable<Boolean> {

    int current;
    int[][] M;
    int vertexCount;
    int start;
    List<Integer> path;

    public SearchCallable(int current, List<Integer> path, int vertexCount, int[][] M, int start) {
        this.current = current;
        this.start = start;
        this.path = path;
        this.vertexCount = vertexCount;
        this.M = M;
    }

    public Boolean search(int current, List<Integer> path, int start) {
        if (path.size() == vertexCount) {
            if (M[current][start] == 1) {
                path.add(start);
                return true;
            }
            return false;
        }

        for (int j = 0; j < vertexCount; j++) {
            if (M[current][j] == 1 && !path.contains(j)) {
                path.add(j);
                boolean result = search(j, path, start);
                if (result) {
                    return true;
                }
                path.remove(path.size() - 1);
            }
        }
        return false;

    }

    @Override
    public Boolean call() throws Exception {
        return search(current, path, start);
    }
}
