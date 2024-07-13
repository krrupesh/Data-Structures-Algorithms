package executors.tut1.api.running;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import executors.tuts.common.CalculationTaskA;
import executors.tuts.common.LoopTaskA;
import executors.tuts.common.NamedThreadsFactory;

public class ReturningValuesUsingExecutors {

	public static void main(String[] args) {

		String  currentThrreadName = Thread.currentThread().getName();
		
		System.out.println("[" + currentThrreadName + "] Main thread starts here...");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		Future<Integer> result1 = execService.submit(new CalculationTaskA(2, 3, 2000));
		Future<Integer> result2 = execService.submit(new CalculationTaskA(3, 4, 1000));
		Future<Integer> result3 = execService.submit(new CalculationTaskA(4, 5, 500));
		
		Future<?> result4 = execService.submit(new LoopTaskA());
		Future<Double> result5 = execService.submit(new LoopTaskA(),999.888);

		execService.shutdown();
		
		try {
			System.out.println("Result-1 "+result1.get());
			System.out.println("Result-2 "+result2.get());
			System.out.println("Result-3 "+result3.get());
			System.out.println("Result-4 "+result4.get());
			System.out.println("Result-5 "+result5.get());

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		System.out.println("[" + currentThrreadName + "] Main thread ends here...");

	}

}
