package top10;

import java.util.LinkedList;
import java.util.Stack;

public class MaxAreaRectangle {

	public static void main(String[] args) {

		//int arr [] = {1,1,2,3};
		
		int arr [] = {2,1,2,3,1};
		
		//findMaxAreaRectangle(arr);
		
		findMaxAreaRect(arr);
	}

	// using stack optimized solution
	public static void findMaxAreaRect(int a[]){
	
		Stack<Integer> stack = new Stack<Integer>();
		int i = 0;
		int top;
		int Area = 0;
		int maxArea = -1;
		
		stack.push(i);
		
		
		for(;i<=a.length;i++){
			
			if(stack.isEmpty() || (a[i] >= a[stack.peek()])){
				stack.push(i);
			}else{
				
				while((!stack.isEmpty()) && a[i] <= a[stack.peek()]){
					top = stack.pop();
					
					System.out.println("top : "+top+" ,i = "+i+" ,a = "+a[i]);

					
					if(stack.isEmpty()){
						Area = a[top] * i;
					}else{
						Area = a[top]*(i - stack.peek() - 1);
					}
					
					if(maxArea < Area){
						maxArea = Area;
					}
				}
		}
		
		System.out.println("maxArea : "+maxArea);
		
		
			
			
			
			 
		}
		
		
	}
	
	
	
	
	// complexity is O(n3)
	public static void findMaxAreaRectangle(int a[]){
		
		int maxArea = 0;
		
		for(int i=0;i<a.length;i++){
			for(int j=0;j<i;j++){
				
				int area = findMin(j,i,a)*(i-j+1);
				
				if(maxArea < area){
					maxArea = area;
				}
				
			}
		}
		
		System.out.println("MaxAreaRectangle : "+maxArea);
	}
	
	public static int findMin(int start, int end, int a[]){
		
		int min = Integer.MAX_VALUE;
		
		for(int i=start;i<=end;i++){
			if(min > a[i]){
				min = a[i];
			}
		}
	return min;	
	}
	
}
