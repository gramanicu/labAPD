package bug5;
/**
 * @author cristian.chilipirea
 * 
 *         Solve the dead-lock
 */
public class MyThreadB implements Runnable {

	@Override
	public void run() {
		synchronized (Main.lockB) {
			synchronized (Main.lockA) {
				for (int i = 0; i < Main.N; i++)
					Main.valueB++;
				for (int i = 0; i < Main.N; i++)
					Main.valueA++;
			}
		}
	}
}
