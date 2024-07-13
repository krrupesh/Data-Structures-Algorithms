package odd.even.printing;


public class Resource {
	
	int num = 1;
	
	public static void main(String args[]){
				
		System.out.println("Thread started");
		
		Resource resource = new Resource();
		
		EvenThread evenThread = new EvenThread(resource);
		evenThread.setName("Even-Thread");
		OddThread oddThread = new OddThread(resource);
		oddThread.setName("Odd-Thread");
				
		evenThread.start();
		oddThread.start();		
	}

	
	public synchronized int getValue(){
		return num;
	}
	
	public synchronized void setValue(Integer num){
		this.num = num;
		System.out.println("setting value to "+num);
	}
}
