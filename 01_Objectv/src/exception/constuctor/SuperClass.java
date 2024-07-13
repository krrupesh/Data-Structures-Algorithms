package exception.constuctor;

public class SuperClass {

	int x;
	String str;
	
	public SuperClass(int x, String str) {
		super();
		this.x = x;
		this.str = str;
		
		System.out.println("SuperClass, x : "+x+" str : "+str);
	}
	
	
}
