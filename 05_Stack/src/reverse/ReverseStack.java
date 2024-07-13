package reverse;

import java.util.LinkedList;
import java.util.Stack;

public class ReverseStack {

	static Stack <Integer>stack;
	
	public static void main(String[] args) {

		stack = new Stack<Integer>();
		
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);

		System.out.println(stack);
		
		reveseStack(stack);
		
		System.out.println(stack);		
	}
	
	
	static int i =1;
	static int j =11;

	
	public static void reveseStack(Stack<Integer> stack){
		
		System.out.println(i++ +" : "+stack);

		
		if(stack.isEmpty()){
			System.out.println("-------------------------");
			return;
		}
		

		int x = stack.pop();
		reveseStack(stack);
		stack.push(x);

		
		System.out.println(j++ +" : "+stack);

		
	}

}
