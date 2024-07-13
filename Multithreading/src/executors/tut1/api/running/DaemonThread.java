package executors.tut1.api.running;

import executors.tuts.common.LoopTaskD;

public class DaemonThread {

	public static void main(String[] args) {

		String currentThreadName = Thread.currentThread().getName();
		System.out.println("["+ currentThreadName + "] Main thread started here...");
		
		Thread t1 = new Thread(new LoopTaskD(500), "Thread-1");
		t1.setDaemon(true);

		Thread t2 = new Thread(new LoopTaskD(1000), "Thread-2");
		//t2.setDaemon(true);
		
		t1.start();
		t2.start();

		System.out.println("["+ currentThreadName + "] Main thread ends here...");

		
	}

}
