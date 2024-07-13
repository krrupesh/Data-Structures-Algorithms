package executors.tuts.common;

import java.util.concurrent.TimeUnit;

public class LoopTaskC implements Runnable {

	private static int count = 0;
	private int instanceNumber;
	private String taskId;

	@Override
	public void run() {
		String currentThreadName = Thread.currentThread().getName();

		System.out.println("####### [" + currentThreadName + "] <" + taskId + "> STARTING ########");

		for (int i = 5; i > 0; i--) {
			System.out.println("[" + currentThreadName + "] <" + taskId + ">TICK TICK " + i);

			try {
				TimeUnit.MILLISECONDS.sleep((long) Math.random() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("####### [" + currentThreadName + "] <" + taskId + "> DONE ########");
	}

	public LoopTaskC() {
		this.instanceNumber = ++count;
		this.taskId = "LoopTaskC" + instanceNumber;
	}
}
