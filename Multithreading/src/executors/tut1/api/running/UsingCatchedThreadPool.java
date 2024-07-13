package executors.tut1.api.running;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import executors.tuts.common.LoopTaskA;

public class UsingCatchedThreadPool {

	public static void main(String[] args) {

		System.out.println("Main thread starts here...");
		
		ExecutorService excecService = Executors.newCachedThreadPool();
		
		excecService.execute(new LoopTaskA());
		excecService.execute(new LoopTaskA());
		excecService.execute(new LoopTaskA());
		excecService.execute(new LoopTaskA());
		excecService.execute(new LoopTaskA());
		excecService.execute(new LoopTaskA());
	
		excecService.shutdown();
		
		excecService.execute(new LoopTaskA());

		
		System.out.println("Main thread ends here...");
	}
}
