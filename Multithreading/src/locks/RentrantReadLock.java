package locks;

public class RentrantReadLock {

	private int readers = 0;
	private int writers = 0;
	private int writeRequests;
	private Thread lockedBy = null;
	private int readersCount = 0;
	
	
	public synchronized void lockRead() throws InterruptedException{
		Thread callingThread = Thread.currentThread();
		
		while((lockedBy != callingThread) || (writers > 0 || writeRequests > 0)){
			wait();
		}
		
		readers++;
		lockedBy = callingThread;
		readersCount++;
	}
	
	public synchronized void unlockRead(){
		readers--;
		
		notifyAll();
	}
	
	
}
