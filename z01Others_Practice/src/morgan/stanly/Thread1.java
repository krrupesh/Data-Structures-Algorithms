package morgan.stanly;

public class Thread1 extends Thread {

	Lock lock;
	
	public Thread1(Lock lock) {
		super();
		this.lock = lock;
	}

	public void run(){
		lock.acquire();
		
		Thread2 t2 = new Thread2(lock);
		t2.setName("Thread-2");
		t2.start();
		
		
		try {
			Thread.sleep(0,5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		lock.release();

	}
}
