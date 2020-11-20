package src.oneProducerOneConsumer;
/**
 * @author cristian.chilipirea
 *
 */
public class Buffer {
	int a;
	volatile static boolean isEmpty = true;


	public synchronized void put(int value) {
		while(!Buffer.isEmpty) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		a = value;
		Buffer.isEmpty = false;
		notify();
	}

	public synchronized int get() {
		while(Buffer.isEmpty) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Buffer.isEmpty = true;
		notify();
		return a;
	}
}
