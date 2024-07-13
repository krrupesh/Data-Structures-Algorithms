package sept21.geeks.stack;

import java.util.Stack;

public class StockSpanProblem {

	public static void main(String[] args) {

		int arr[] = {100, 80, 60, 70, 60, 75, 85};

		
		
		
	}
	
	
	
	public static void findSpan(int []arr){
		
		Stack<Integer> stack = new Stack<>();
		
		stack.push(arr[0]);
		System.out.println(1);
		
		
		for(int i=1;i<arr.length;i++){
			
			int top = stack.peek();
			
			while(!stack.isEmpty() && arr[top] <= arr[i]){
				stack.pop();
			}
			
			System.out.println(stack.isEmpty() ? i : (i-top));
			
			stack.push(arr[i]);			
			
		}
		
		
		
	}
	
	
	

}
