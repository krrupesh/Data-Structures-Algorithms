package producer.consumer;

import java.util.List;
import java.util.Queue;

public class Consumer extends Thread{
	
	Queue<Integer> queue;
	int i;
	
	public Consumer(Queue<Integer> queue) {
		this.queue = queue;
	}

	public void run(){
		while(true){
			consume(queue);
		}
	}
	
	public void consume(Queue<Integer> queue){
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		synchronized (queue) {
			while(queue.size() == 0){
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			int value = queue.poll();
			
			System.out.println("Consumed value "+ value);
			
			queue.notify();
						
		}
		
	}
}
