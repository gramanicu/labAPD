package task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    static int N = 10;
    static int COLORS = 3;
    static int[][] graph = { { 0, 1 }, { 0, 4 }, { 0, 5 }, { 1, 0 }, { 1, 2 }, { 1, 6 }, { 2, 1 }, { 2, 3 }, { 2, 7 },
            { 3, 2 }, { 3, 4 }, { 3, 8 }, { 4, 0 }, { 4, 3 }, { 4, 9 }, { 5, 0 }, { 5, 7 }, { 5, 8 }, { 6, 1 },
            { 6, 8 }, { 6, 9 }, { 7, 2 }, { 7, 5 }, { 7, 9 }, { 8, 3 }, { 8, 5 }, { 8, 6 }, { 9, 4 }, { 9, 6 },
            { 9, 7 } };

    public static void main(String[] args) {
        ExecutorService tpe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int[] colors = new int[N];
        tpe.submit(new MyRunnable(0, tpe, colors));
    }
}

class MyRunnable implements Runnable {
    ExecutorService tpe;
    int[] colors;
    int step;

    private static boolean verifyColors(int[] colors, int step) {
        for (int i = 0; i < step; i++) {
            if (colors[i] == colors[step] && isEdge(i, step))
                return false;
        }
        return true;
    }

    private static boolean isEdge(int a, int b) {
        for (int[] ints : Main.graph) {
            if (ints[0] == a && ints[1] == b)
                return true;
        }
        return false;
    }

    static void printColors(int[] colors) {
        StringBuilder aux = new StringBuilder();
        for (int color : colors) {
            aux.append(color).append(" ");
        }
        System.out.println(aux);
    }

    public MyRunnable(int step, ExecutorService tpe, int[] colors) {
        this.step = step;
        this.tpe = tpe;
        this.colors = colors;
    }

    @Override
    public void run() {
        if (step == Main.N) {
            printColors(colors);

            // Shutdown daca nu mai sunt taskuri in queue
            ThreadPoolExecutor helper = (ThreadPoolExecutor) this.tpe;
            if(helper.getTaskCount() - helper.getActiveCount() - helper.getCompletedTaskCount() == 0) {
                tpe.shutdown();
            }
            return;
        }

        // for the node at position step try all possible colors
        for (int i = 0; i < Main.COLORS; i++) {
            int[] newColors = colors.clone();

            newColors[step] = i;
            if (verifyColors(newColors, step))
                tpe.submit(new MyRunnable(step + 1, tpe, newColors));
        }
    }
}