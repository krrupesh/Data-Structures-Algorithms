package odd.even.print.working;

public class ThreeThreads {

	int counter = 1;

	static int N;

	// print odd numbers
	public void print1() {
		synchronized (this) {
			while (counter < N) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (counter % 2 == 0  && counter % 3 == 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.print(counter + " ");
				counter++;
				notify();
			}
		}
	}

	// Function to print even numbers
	public void print2() {
		synchronized (this) {
			while (counter < N) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (counter % 2 == 1) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.print(counter + " ");
				counter++;
				notify();
			}
		}
	}

	
	// Function to print even numbers
	public void print3() {
		synchronized (this) {
			while (counter < N) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (counter % 3 != 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.print(counter + " ");
				counter++;
				notify();
			}
		}
	}
	
	// Driver Code
	public static void main(String[] args) {
		// Given Number N
		N = 10;

		// Create an object of class
		final ThreeThreads mt = new ThreeThreads();

		// Create thread t1
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				mt.print1();
			}
		});

		// Create thread t2
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				mt.print2();
			}
		});

		// Create thread t2
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				mt.print3();
			}
		});
		
		// Start both threads
		t1.start();
		t2.start();
		t3.start();
	}
}