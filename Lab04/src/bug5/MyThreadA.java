package bug5;
/**
 * @author cristian.chilipirea
 * 
 *         Solve the dead-lock
 */
public class MyThreadA implements Runnable {

	@Override
	public void run() {
		synchronized (Main.lockA) {
			synchronized (Main.lockB) {
				for (int i = 0; i < Main.N; i++)
					Main.valueA++;
				for (int i = 0; i < Main.N; i++)
					Main.valueB++;
			}
		}
	}
}
