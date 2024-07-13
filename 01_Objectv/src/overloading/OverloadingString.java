package overloading;

public class OverloadingString {

	String st=null;

	public static void main(String[] args) {

		OverloadingString obj = new OverloadingString();
		obj.print(null);
		
	}
	
	
	public void print(String str){
		System.out.println("String");
		System.out.println(st);

	}
	
	public void print(Object obj){
		System.out.println("Object");
	}

}
