package locks;

public class ReadWriteLock {

	private int readers = 0;
	private int writers = 0;
	private int writeRequests;
	
	
	public synchronized void lockRead() throws InterruptedException{
		
		while(writers > 0 || writeRequests > 0){
			wait();
		}
		
		readers++;
	}
	
	public synchronized void unlockRead(){
		readers--;
		
		notifyAll();
	}	
	
	public synchronized void lockWrite() throws InterruptedException{
		writeRequests++;
		
		while(readers>0 || writers>0){
			wait();
		}
		
		writeRequests--;
		writers++;
	}
	
	public synchronized void unlockWrite(){
		writers--;
		
		notifyAll();
	}
}
