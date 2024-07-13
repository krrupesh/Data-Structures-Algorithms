package completable.future;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FutureTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Future<Integer> future = executorService.submit(new Task());
		System.out.println("Start at "+ new Date());
		
		Thread.sleep(3000);
		System.out.println("After sleep "+ new Date());

		
		try {
			Integer value = future.get();
			System.out.println("value is "+ value + " at "+ new Date());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	static class Task implements Callable<Integer>{

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			
			Thread.sleep(5000);
			
			return 10;
		}
		
	}

}
