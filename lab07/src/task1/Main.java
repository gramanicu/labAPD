package task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static int[][] graph = { { 0, 1 }, { 0, 4 }, { 0, 5 }, { 1, 0 }, { 1, 2 }, { 1, 6 }, { 2, 1 }, { 2, 3 }, { 2, 7 },
            { 3, 2 }, { 3, 4 }, { 3, 8 }, { 4, 0 }, { 4, 3 }, { 4, 9 }, { 5, 0 }, { 5, 7 }, { 5, 8 }, { 6, 1 },
            { 6, 8 }, { 6, 9 }, { 7, 2 }, { 7, 5 }, { 7, 9 }, { 8, 3 }, { 8, 5 }, { 8, 6 }, { 9, 4 }, { 9, 6 },
            { 9, 7 } };

    public static void main(String[] args) {
        ExecutorService tpe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Integer> partialPath = Collections.synchronizedList(new ArrayList<>());
        partialPath.add(0);

        // From 0 to 3
        tpe.submit(new MyRunnable(3, tpe, partialPath));
    }
}

class MyRunnable implements Runnable {
    ExecutorService tpe;
    List<Integer> partialPath;
    int destination;

    public MyRunnable(int destination, ExecutorService tpe, List<Integer> partialPath) {
        this.partialPath = partialPath;
        this.tpe = tpe;
        this.destination = destination;
    }

    @Override
    public void run() {
        if (partialPath.get(partialPath.size() - 1) == destination) {
            System.out.println(partialPath);

            // Shutdown daca nu mai sunt taskuri in queue
            ThreadPoolExecutor helper = (ThreadPoolExecutor) this.tpe;
            if(helper.getTaskCount() - helper.getActiveCount() - helper.getCompletedTaskCount() == 0) {
                tpe.shutdown();
            }
            return;
        }

        // se verifica nodurile pentru a evita ciclarea in graf
        int lastNodeInPath = partialPath.get(partialPath.size() - 1);
        for (int[] ints : Main.graph) {
            if (ints[0] == lastNodeInPath) {
                if (partialPath.contains(ints[1]))
                    continue;
                ArrayList<Integer> newPartialPath = new ArrayList<>(partialPath);
                newPartialPath.add(ints[1]);

                tpe.submit(new MyRunnable(destination, tpe, newPartialPath));
            }

        }


    }
}
