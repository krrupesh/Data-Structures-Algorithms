package newhiresetB.print.elements.counter.sorted.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PrintElements {

	public static void main(String[] args) {


		int arr[] = {10,23,12,5,6,10,23,5,10,5,12,-1,-1,-1,23,10,12,10,10,23};
		
		//int arr[] = {8,7,5,6,4,2,3,1};

		
		int a[] = manipulateArray(arr);
		
		for(int i =0;i<a.length;i++){
			System.out.print(a[i]+",");
		}
		
			
		/*Map map = getElementCounterMap(arr);
						
		int [][] sortedarr2D = getSorted2DArray(get2DArray(getElementCounterMap(arr)));
				
		System.out.println("===================");
		for(int i=0;i<sortedarr2D.length;i++){			
			for(int j=0;j<sortedarr2D[i][1];j++){
				System.out.print(sortedarr2D[i][0]+",");
			}
		}*/
	
	}
	
	public static int[] manipulateArray(int [] inputArray){
		 
		int size = inputArray.length;
		
		int arr[] = new int[size];
		
		int [][] sortedarr2D = getSorted2DArray(get2DArray(getElementCounterMap(inputArray)));
		
		for(int i=0,k=0;i<sortedarr2D.length;i++){
			for(int j=0;j<sortedarr2D[i][1];j++){
               arr[k]=sortedarr2D[i][0];
               k++;
 			}
		}

		return arr;
	}
	
		
	// can decrease to nlogn
	public static int[][] getSorted2DArray(int[][] arr){		
		
		 int n = arr.length;
         int temp0 = 0;
         int temp1 = 0;
        
         for(int i=0; i < n; i++){
             for(int j=1; j < (n-i); j++){
                    
                 if(arr[j-1][1] < arr[j][1]){
                	 
                     //swap the elements!
            	 	 temp1 = arr[j-1][1];                       	 	 
                     arr[j-1][1] = arr[j][1];
                     arr[j][1] = temp1;
                     
            	 	 temp0 = arr[j-1][0];
                     arr[j-1][0] = arr[j][0];
                     arr[j][0] = temp0;
                         
                 }else if(arr[j-1][1] == arr[j][1]){
                	 if(arr[j-1][0] > arr[j][0]){
                		 
                	 	 temp0 = arr[j-1][0];                        	 	 
                         arr[j-1][0] = arr[j][0];
                         arr[j][0] = temp0;
                                                 	
                	 }else{
                		 
                	 }
                 }
                   
             }
         }
		
		return arr;
	}
	
	public static int[][] get2DArray(Map map){
		int arr[][] = new int[map.size()][2];
		
		Set<Integer> set = map.entrySet();
		
		Set mapSet = (Set) map.entrySet();
        //Create iterator on Set 
        Iterator mapIterator = mapSet.iterator();
        int i = 0;
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            Integer keyValue = (Integer) mapEntry.getKey();
            Integer value = (Integer) mapEntry.getValue();
            
            arr[i][0] = keyValue;
            arr[i][1] = value;
            
            i++;
        }
		
		return arr;
	}
	
	public static Map getElementCounterMap(int[] arr){
		
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
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
	
	


}
