package odd.even.printing;


public class OddThread extends Thread {

	Resource resource;
	int num = 0;

	public OddThread(Resource resource) {
		this.resource = resource;
	}

	public void run() {
		while (true) {
			printOdd(resource);
		}
	}

	public void printOdd(Resource resource) {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		

		int value = resource.getValue();

		synchronized (resource) {

			while (value %2 == 0) {
				try {
					resource.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println(Thread.currentThread().getName()+" - " + value);
			resource.setValue(++value);
			
			//System.out.println("Step-1 "+Thread.currentThread().getName());
			resource.notify();
			System.out.println("Step-2 "+Thread.currentThread().getName());


		}
	}
}
