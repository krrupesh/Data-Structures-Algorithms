package producer.consumer;

import java.util.Queue;

public class Producer extends Thread {

	Queue<Integer> queue;
	int size;
	int i;

	public Producer(Queue<Integer> queue, int size) {
		this.queue = queue;
		this.size = size;
	}

	public void run() {
		while (true) {
			produce(queue, size);
		}
	}

	public void produce(Queue<Integer> queue, int size) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		synchronized (queue) {
			while (queue.size() >= size) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			queue.add(++i);
			System.out.println("Producer adding value " + i);

			queue.notify();

		}

	}

}
