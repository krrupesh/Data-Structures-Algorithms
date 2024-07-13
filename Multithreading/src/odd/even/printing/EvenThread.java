package odd.even.printing;

public class EvenThread extends Thread {

	Resource resource;

	public EvenThread(Resource resource) {
		this.resource = resource;
	}

	public void run() {
		while (true) {
			printEven(resource);
		}
	}

	public void printEven(Resource resource) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		//System.out.println("Step-1 "+Thread.currentThread().getName());
		int value = resource.getValue();
		//System.out.println("Step-2 "+Thread.currentThread().getName());


		synchronized (resource) {
			//System.out.println("Step-3 "+Thread.currentThread().getName());

			while (value %2 != 0) {
				try {
					resource.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println(Thread.currentThread().getName()+" - " + value);
			resource.setValue(++value);

			resource.notify();

		}

	}

}
