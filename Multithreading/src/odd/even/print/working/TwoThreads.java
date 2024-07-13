package odd.even.print.working;

public class TwoThreads {
	int counter = 1;
	static int N;

	// print odd numbers
	public void printOddNumber() {
		System.out.println("inside print odd");
		synchronized (this) {
			while (counter < N) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (counter % 2 == 0) {
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
	public void printEvenNumber() {
		System.out.println("inside print even");
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

	// Driver Code
	public static void main(String[] args) {
		// Given Number N
		N = 10;

		// Create an object of class
		final TwoThreads mt = new TwoThreads();

		// Create thread t1
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				mt.printEvenNumber();
			}
		});

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Create thread t2
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				mt.printOddNumber();
			}
		});

		// Start both threads
		t1.start();
		t2.start();
	}
}