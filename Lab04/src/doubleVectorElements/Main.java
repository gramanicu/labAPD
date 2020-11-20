package doubleVectorElements;
/**
 * @author cristian.chilipirea
 *
 */
public class Main {
	public static class Doubler extends Thread {
		int thread_id;

		public Doubler(int id) {
			this.thread_id = id;
		}

		public void run() {
			int start = thread_id * Main.N / Main.threads;
			int end = Math.min((thread_id + 1) * Main.N / Main.threads, Main.N);

			for(int i = start; i < end; ++i) {
				v[i] *= 2;
			}
		}
	}


	static int N = 100000013;
	static int[] v = new int[N];
	static int threads;
	public static void main(String[] args) {
		 threads = Runtime.getRuntime().availableProcessors();

		for(int i=0;i<N;i++)
			v[i]=i;
		
		Thread[] t = new Thread[threads];

		for(int i = 0; i < threads; ++i) {
			t[i] = new Doubler(i);
			t[i].start();
		}

		for(int i = 0; i < threads; ++i) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < N; i++) {
			if(v[i]!= i*2) {
				System.out.println("Wrong answer");
				System.exit(1);
			}
		}
		System.out.println("Correct");
	}

}
