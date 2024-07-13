package executors.tut1.api.running;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import executors.tuts.common.LoopTaskA;
import executors.tuts.common.LoopTaskC;
import executors.tuts.common.NamedThreadsFactory;

public class UsingCatchedThreadPoolPart2 {

	public static void main(String[] args) throws InterruptedException {

		System.out.println("Main thread starts here...");
		
		ExecutorService excecService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		excecService.execute(new LoopTaskC());
		excecService.execute(new LoopTaskC());
		excecService.execute(new LoopTaskC());
		
		Thread.sleep(10000);
		
		
		excecService.execute(new LoopTaskC());
		excecService.execute(new LoopTaskC());
		excecService.execute(new LoopTaskC());
	
		excecService.shutdown();
		
		//excecService.execute(new LoopTaskA());

		
		System.out.println("Main thread ends here...");
	}
}
