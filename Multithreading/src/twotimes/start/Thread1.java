package twotimes.start;

public class Thread1 extends Thread{

	public void run(){
		try {
			System.out.println("state 3 : "+Thread.currentThread().getState());

			Thread.sleep(2000);
			
			System.out.println("state 4 : "+Thread.currentThread().getState());

			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Thread1.run()");
	}
}
