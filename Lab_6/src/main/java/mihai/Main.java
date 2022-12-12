package mihai;

public class Main {

    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.generateGraph(10, 50);
        solver.searchHamiltonianCycle(0);
    }
}

