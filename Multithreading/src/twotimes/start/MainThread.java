package twotimes.start;

public class MainThread {

	public static void main(String[] args) {

		Thread1 t1 = new Thread1();
		t1.start();
		
		System.out.println(t1.getId());

		
		System.out.println("state 1 : "+t1.getState());

		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("state 2 : "+t1.getState());

		
		System.out.println(t1.getId());
		
		System.out.println("after 10 secs");
		
		t1.start();
		
		
	}

}
