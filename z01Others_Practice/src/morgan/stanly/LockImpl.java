package morgan.stanly;

public class LockImpl implements Lock{

	public boolean lockEnabled = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Lock lock = new LockImpl();
		Thread1 t1 = new Thread1(lock);
		t1.setName("Thread-1");
		t1.start();

	}

	@Override
	public void acquire() {

		if(!this.lockEnabled){
			this.lockEnabled = true;
			if(Thread.currentThread().getName().equalsIgnoreCase("Thread-1")){
				System.out.println("Thread1 acquired lock");
			}else if(Thread.currentThread().getName().equalsIgnoreCase("Thread-2")){
				System.out.println("Thread2 acquired lock");
			}
		}else{
			while(this.lockEnabled){
				if(this.lockEnabled){
					if(Thread.currentThread().getName().equalsIgnoreCase("Thread-1")){
						System.out.println("Thread1 waiting for lock");
					}else if(Thread.currentThread().getName().equalsIgnoreCase("Thread-2")){
						System.out.println("Thread2 waiting for lock");
					}
				}else{
					if(Thread.currentThread().getName().equalsIgnoreCase("Thread-1")){
						System.out.println("Thread1 acquired lock");
					}else if(Thread.currentThread().getName().equalsIgnoreCase("Thread-2")){
						System.out.println("Thread2 acquired lock");
					}
				}
			}
		}
		

	}

	@Override
	public void release() {

         if(this.lockEnabled){
        	 this.lockEnabled = false;
				if(Thread.currentThread().getName().equalsIgnoreCase("Thread-1")){
					System.out.println("Thread1 released lock");
				}else if(Thread.currentThread().getName().equalsIgnoreCase("Thread-2")){
					System.out.println("Thread2 released lock");
				}
         }
		
	}

}
