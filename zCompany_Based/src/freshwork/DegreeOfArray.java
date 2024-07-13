package freshwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DegreeOfArray {

	public static void main(String[] args) {

		List<Integer> arr =  new ArrayList<Integer>();
		arr.add(1);
		arr.add(2);
		arr.add(1);
		arr.add(3);
		arr.add(2);
		arr.add(1);
		arr.add(3);
		arr.add(4);
		
		int diff = degreeOfArray(arr);
		System.out.println(diff);
	}

	
	 public static int degreeOfArray(List<Integer> arr) {

		 HashMap<Integer,Index> map = new HashMap<Integer,Index>();
		 Index index;
		 int maxCount = 0;
		 Integer key = 0;
		 int j = 0;
		 
		 if(arr.size() == 0){
			 return 0;
		 }
		 
		 for(Integer i : arr){
			 
			 if(!map.containsKey(i)){
				
				 index = new Index(j,j,1);
				 map.put(i, index);
				 
			 }else{
				 index = map.get(i);
				 index.endIndex = j;
				 index.count = index.count + 1;
				 
				 map.put(i, index);
			 }
			 
			 j++;
			
			 
			 if(maxCount < index.count){
				 maxCount = index.count;
				 key = i;
				 
			 }
			 
		 }

		 index = map.get(key);
		 
		 return index.endIndex - index.startIndex + 1;
		 
	 }
		    
    static class Index{
    	
        int startIndex;
        int endIndex;
        int count;
        
		public Index(int startIndex, int endIndex, int count) {
			super();
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.count = count;
		}
            
        
    }
}
