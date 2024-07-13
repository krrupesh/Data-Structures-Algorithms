package lec10;

public class TypeInference {

	public static void main(String[] args) {

		StringLengthLambda mylambda = s->s.length();
		
		System.out.println(mylambda.getLength("Hello world!"));
		
	}

}


// with the method defined inside the functional interface the compiler gets the 
// information about the type of arguments, no of arguments and return type of method
interface StringLengthLambda{
	int getLength(String s);
}