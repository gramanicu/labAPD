package src.multipleProducersMultipleConsumersNBuffer;

import java.util.Queue;

/**
 * @author Gabriel Gutu <gabriel.gutu at upb.ro>
 *
 */
public class Buffer {
    Queue queue;
    int limit;
    boolean isFull = false;
    
    public Buffer(int size) {
        queue = new LimitedQueue(size);
        limit = size;
    }

	public synchronized void put(int value) {
        while(isFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.add(value);

        if(queue.size() == limit) {
            isFull = true;
        }

        notify();
	}

    public synchronized int get() {
        while(queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        notify();
        isFull = false;

        return (int)queue.poll();
	}
}

