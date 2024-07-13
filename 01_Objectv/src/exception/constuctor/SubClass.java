package exception.constuctor;

public class SubClass extends SuperClass{

	int x;
	String str;
	
	String name;

	public SubClass(int x, String str, String name)  {
		super(x,str);
		this.x = x;
		this.str = str;
		this.name = name;
		
		System.out.println("SubClass, x : "+x+" str : "+str+" name : "+name);

	}

}
