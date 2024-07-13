package lec09;

public class Greeter {

	public void greet(Greeting greeting){
		greeting.perform();
	}
	
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		
		Greeting helloWorldGreeting = new HelloWorldGreeting();
		Greeting lambdaGreeting = () -> System.out.println("Hello World!");
		
		lambdaGreeting.perform();
	}

}
