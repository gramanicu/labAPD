package shortestPathsFloyd_Warshall;
/**
 * @author cristian.chilipirea
 *
 */
public class Main {
	public static class GraphThread extends Thread {
		int graph[][];
		int thread_id;
		int M;
		int tCount;

		public GraphThread(int[][] pGraph, int thread_id, int m, int tCount) {
			this.graph = pGraph;
			this.thread_id = thread_id;
			this.M = m;
			this.tCount = tCount;
		}

		public void run() {
			int start = thread_id * 5 / tCount;
			int end = Math.min((thread_id + 1) * 5 / tCount, 5);

			for (int k = start; k < end; k++) {
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 5; j++) {
						graph[i][j] = Math.min(graph[i][k] + graph[k][j], graph[i][j]);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		int M = 9;
		int graph[][] = { { 0, 1, M, M, M }, 
				          { 1, 0, 1, M, M }, 
				          { M, 1, 0, 1, 1 }, 
				          { M, M, 1, 0, M },
				          { M, M, 1, M, 0 } };

		int pGraph[][] = graph.clone();
		int threads = Math.min(5, Runtime.getRuntime().availableProcessors());
		
		// Compute sequentially the graph
		for (int k = 0; k < 5; k++) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					graph[i][j] = Math.min(graph[i][k] + graph[k][j], graph[i][j]);
				}
			}
		}


		// Create, start and join threads
		Thread[] t = new Thread[threads];
		for(int i = 0; i < threads; ++i) {
			t[i] = new GraphThread(pGraph, i, M, threads);
			t[i].start();
		}

		for(int i = 0; i < threads; ++i) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		boolean is_ok = true;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(graph[i][j] != pGraph[i][j]) {
					is_ok = false;
				}
			}
		}

		if(is_ok) {
			System.out.println("The results are good!");
			System.out.println();
		} else {
			System.out.println("The results are bad!");
			System.out.println();
		}

		// Print graph
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(graph[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println();

		// Print parallel graph
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(pGraph[i][j] + " ");
			}
			System.out.println();
		}
	}
}
