package find.max.sum;

import java.util.HashMap;
import java.util.Map;

public class FindMaxSum {

	public static Map<Integer,Integer> map = new HashMap<Integer,Integer>();

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		//int a[] = {2,8,4,10,6};
		int a[] = {1500000000,8,4,13000000,6,1500000000,50,60,1500000000,75,90,15000000};
		System.out.println(findMax(a,1500000000));
		
		System.out.println("Time taken :  "+(System.currentTimeMillis()-start));
	}
	

	public static Map getElementCounterMap(int[] arr){
		
		Integer count = 0;
		
		for(int i = 0;i<arr.length;i++){
			count = map.get(arr[i]);
			if(count!=null){
				map.put(arr[i], ++count);
			}else{
				count = 0;
				map.put(arr[i], ++count);
			}
		}
		
		return map;
	}
	
	public static int getNoOfElements(int key){
		Integer count = 0;
		count =map.get(key);
		
		if(count!=null){
			return count;
		}else{
			return 0;
		}		
	}
	
	public static long findMax(int []a, long k){
		
		
		
		long maxsum =0;		
		int n = a.length;	
		int i = 0;
		int count = 0;
		int valuek = 0;
		int valueai = 0;
		int forcounter = 0;
		
		valueai = getMax(a);
		
		int m =0;
		
		getElementCounterMap(a);

		
		while(true){
			
			count = getNoOfElements(valueai);
			
			//count = findNoOfElementsInArrayOfSameValue(a,valueai);
						
			if(count ==0){
				count = 1;
			}else{
				forcounter = forcounter+count;
			}
			
			if(valuek == 0){
				forcounter = count;
			}
			
			for(int j=0;j<forcounter;j++){
				maxsum = maxsum+valueai;
				
				valuek++;
				
				if(valuek==k){
					System.out.println("Value equal to k valuek "+valuek);
					break;
				}
				
			}
			
			if(valuek==k){
				System.out.println("Value equal to k valuek "+valuek);
				break;
			}
			
			if(valueai<=0){
				System.out.println("Value less than 0  valuek "+valuek);
				break;
			}

			valueai--;
			
			
			
		}		
		
		return maxsum;
	}
	
	  public static int getMax(int[] inputArray){ 
		    int maxValue = inputArray[0]; 
		    for(int i=1;i < inputArray.length;i++){ 
		      if(inputArray[i] > maxValue){ 
		         maxValue = inputArray[i]; 
		      } 
		    } 
		    return maxValue; 
		  }
	
	public static int findNoOfElementsInArrayOfSameValue(int []arr, int value){
		int count = 0;
		
		for(int i=0;i<arr.length;i++){
			if(arr[i]==value){
				count++;
			}
		}
		
		return count;
	}

}
