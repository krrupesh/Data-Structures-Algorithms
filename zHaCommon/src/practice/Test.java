package practice;

import java.util.PriorityQueue;

public class Test {


	public static void main(String[] args) {
		int arr[] = {11,2,8,1,6,6,4,10,5,3,0,8};
		
		int marks = marksForRank(arr, 3);
		
		System.out.println("marks : "+marks);
	}

	
	  static int marksForRank(int[] nums, int k) {
		  PriorityQueue<Integer> q = new PriorityQueue<Integer>(k);
		  for(int i: nums){
			  
		      if(q.size()>k){
		          q.poll();
		      }
			  
			  if(!q.contains(i)){
			      q.offer(i);  
			  }
		
			  System.out.println("offer "+q);
			  

		      
		      System.out.println("poll "+q);
		  }

  return q.peek();

  }

}
