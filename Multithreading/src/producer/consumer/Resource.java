package producer.consumer;

import java.util.LinkedList;
import java.util.Queue;

public class Resource {
	
	public static void main(String args[]){
				
		Queue<Integer> queue = new LinkedList<>();

		System.out.println("Thread started");
		
		Producer producer = new Producer(queue, 5);
		producer.setName("Producer-Thread");
		Consumer consumer = new Consumer(queue);
		consumer.setName("Consumer-Thread");
				
		producer.start();
		consumer.start();		
	}
	
	
	/*try {
		producer.join();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}*/


	/*
	 
	 (select * from Employee group by dept_id order by salary desc) limit 3)
	 
	 
	 
	 * 
	 */
	
}
