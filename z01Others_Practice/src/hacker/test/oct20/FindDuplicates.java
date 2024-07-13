package hacker.test.oct20;

import java.util.HashMap;
import java.util.Map;

public class FindDuplicates{

	public static void main(String args[]){
	
		int []arr = {1,2,3,2,3,4,5,5,6,7};
		findDuplicates(arr);
	}

	public static void findDuplicates(int arr[]){
	
	  Map<Integer,Integer> map = new HashMap<Integer,Integer>();	
	  Integer count = 0;
	  
	   for(int i=0;i<arr.length;i++){
	      
		  count = map.get(arr[i]); // count == null,
		  
		  if(count != null){
		     map.put(arr[i],++count);
		  }else{
		    map.put(arr[i],1); // [1-1,2-2,3-1]
		  }	   
	   }	  

	   System.out.println(map);
	  
	}
	
}
