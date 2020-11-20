package coreCount;

public class Main {
    public static class HelloWorldThread extends Thread {
        private int id;
        public HelloWorldThread(int id) {
            this.id = id;
        }

        public void run() {
            System.out.println("This is thread number " + id);
        }
    }

    public static void main(String[] args) {
        int threads = Runtime.getRuntime().availableProcessors();
        System.out.println("You have " + threads +  " threads available");
        Thread[] t = new Thread[threads];

        for(int i = 0; i < threads; ++i) {
            t[i] = new HelloWorldThread(i);
            t[i].start();
        }

        for(Thread th : t) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
