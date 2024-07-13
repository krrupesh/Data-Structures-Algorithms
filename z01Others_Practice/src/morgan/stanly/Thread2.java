package morgan.stanly;

public class Thread2 extends Thread {

	Lock lock;
	
	public Thread2(Lock lock) {
		super();
		this.lock = lock;
	}

	public void run(){
		lock.acquire();
	}
}
