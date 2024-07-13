package thread.examples;

public class OverrideStartMethod {

	public static void main(String[] args) {

		Thread1 th1 = new Thread1();
		th1.setName("Rupesh");
		//th1.run();
		th1.start();
			
		System.out.println("Thread started");
	}

}
