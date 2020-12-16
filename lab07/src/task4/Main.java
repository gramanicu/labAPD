package task4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {
    static int[][] graph = { { 0, 1 }, { 0, 4 }, { 0, 5 }, { 1, 0 }, { 1, 2 }, { 1, 6 }, { 2, 1 }, { 2, 3 }, { 2, 7 },
            { 3, 2 }, { 3, 4 }, { 3, 8 }, { 4, 0 }, { 4, 3 }, { 4, 9 }, { 5, 0 }, { 5, 7 }, { 5, 8 }, { 6, 1 },
            { 6, 8 }, { 6, 9 }, { 7, 2 }, { 7, 5 }, { 7, 9 }, { 8, 3 }, { 8, 5 }, { 8, 6 }, { 9, 4 }, { 9, 6 },
            { 9, 7 } };

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        ArrayList<Integer> partialPath = new ArrayList<>();
        // se vor calcula toate caile de la nodul 0 la nodul 3 in cadrul grafului
        partialPath.add(0);

        fjp.invoke(new MyTask(partialPath, 3));
        fjp.shutdown();
    }
}

class MyTask extends RecursiveTask<Void> {
    ArrayList<Integer> partialPath;
    int destination;

    public MyTask(ArrayList<Integer> partialPath, int destination) {
        this.partialPath = partialPath;
        this.destination = destination;
    }

    @Override
    protected Void compute() {
        if (partialPath.get(partialPath.size() - 1) == destination) {
            System.out.println(partialPath);
            return null;
        }

        List<MyTask> pathsList = new ArrayList<>();

        // se verifica nodurile pentru a evita ciclarea in graf
        int lastNodeInPath = partialPath.get(partialPath.size() - 1);
        for (int[] ints : Main.graph) {
            if (ints[0] == lastNodeInPath) {
                if (partialPath.contains(ints[1]))
                    continue;

                ArrayList<Integer> newPartialPath = new ArrayList<>(partialPath);
                newPartialPath.add(ints[1]);

                MyTask newTask = new MyTask(newPartialPath, destination);
                pathsList.add(newTask);
                newTask.fork();
            }
        }

        pathsList.forEach(MyTask::join);
        return null;
    }
}