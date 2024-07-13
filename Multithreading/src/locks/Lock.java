package locks;

/*
 * make the class Reentrant
 */
public class Lock {

	private boolean isLocked = false;
	private Thread lockedBy = null;
	private int lockedCount = 0;
	
	public synchronized void lock() throws InterruptedException{
		Thread callingThread = Thread.currentThread();
		while(isLocked && (lockedBy != callingThread)){
			wait();
		}
		
		isLocked = true;
		lockedBy = callingThread;
		lockedCount++;

	}
	
	
	public synchronized void unlock(){
		
		if(this.lockedBy == Thread.currentThread()){
			lockedCount--;
			
			if(lockedCount == 0){
				isLocked = false;
				notify();

			}

		}
		
	}
	
}
