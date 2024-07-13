package lec21;

public class MethodReferenceExample1 {

	public static void main(String[] args) {

		Thread thread = new Thread(MethodReferenceExample1::printMessage);//Thread(()->printMessage())
		thread.start();		
	}

	public static void printMessage(){
		System.out.println("Hello");
	}
}
