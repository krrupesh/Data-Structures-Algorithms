package executors.tut1.api.running;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import executors.tuts.common.CalculationTaskB;
import executors.tuts.common.LoopTaskA;
import executors.tuts.common.NamedThreadsFactory;
import executors.tuts.common.TaskResult;

public class ReturningValuesUsingExecutorsSecondWay {

	public static void main(String[] args) {

		String  currentThrreadName = Thread.currentThread().getName();
		
		System.out.println("[" + currentThrreadName + "] Main thread starts here...");
		
		ExecutorService execService = Executors.newCachedThreadPool(new NamedThreadsFactory());
		
		CompletionService<TaskResult<String, Integer>> tasks = 
				new ExecutorCompletionService<TaskResult<String, Integer>>(execService);
		
		tasks.submit(new CalculationTaskB(2, 3, 2000));
		tasks.submit(new CalculationTaskB(3, 4, 1000));
		tasks.submit(new CalculationTaskB(4, 5, 500));
		
		//Future<?> result4 = execService.submit(new LoopTaskA());
		tasks.submit(new LoopTaskA(),new TaskResult<String, Integer>("LoopTaskA-1",999));

		execService.shutdown();
		
		for (int i = 0; i < 4; i++) {
			try {
				System.out.println(tasks.take().get());

			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		System.out.println("[" + currentThrreadName + "] Main thread ends here...");

	}

}
