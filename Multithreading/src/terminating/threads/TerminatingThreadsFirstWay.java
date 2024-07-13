package terminating.threads;

import java.util.concurrent.TimeUnit;

import executors.tuts.common.LoopTaskE;

public class TerminatingThreadsFirstWay {

	public static void main(String[] args) throws InterruptedException {

		String currentThread = Thread.currentThread().getName();
		System.out.println("[" + currentThread + "] Main thread starts here...");
		
		
		LoopTaskE task1 = new LoopTaskE();
		LoopTaskE task2 = new LoopTaskE();
		LoopTaskE task3 = new LoopTaskE();
		
		new Thread(task1, "MyThread-1").start();
		new Thread(task1, "MyThread-1").start();
		new Thread(task1, "MyThread-1").start();
		
		TimeUnit.MICROSECONDS.sleep(5000);
		
		task1.cancel();
		task2.cancel();
		task3.cancel();
	
		System.out.println("[" + currentThread + "] Main thread ends here...");

		
		
	}

}
