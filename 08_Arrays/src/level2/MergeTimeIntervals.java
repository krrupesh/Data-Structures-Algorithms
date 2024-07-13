package level2;

import java.util.*;

public class MergeTimeIntervals {

	public static void main(String[] args) {
        System.out.println("Hello, world!");
        
        List<Integer> l1 = new ArrayList();
        l1.add(2);
        l1.add(4);
        
        List<Integer> l2= new ArrayList();
        l2.add(1);
        l2.add(3);
        
                
        List<Integer> l3 = new ArrayList();
        l3.add(6);
        l3.add(8);

        
        List<Integer> l4 = new ArrayList();
        l4.add(5);
        l4.add(7);

        
        List<List<Integer>> originalList = new ArrayList();
        originalList.add(l1);
        originalList.add(l2);
        originalList.add(l3);
        originalList.add(l4);
        
       List<List<Integer>> mergedList =  mergeArrays(originalList);
        
       System.out.println(mergedList); 
        
    }
    
    
    public static List<List<Integer>>  mergeArrays(List<List<Integer>> originalList){
        
        for(List<Integer> timeInterval1 : originalList ){
            for(List<Integer> timeInterval2 : originalList){

                if(timeInterval1!=timeInterval2){
                    if(timeInterval1.get(1)>timeInterval2.get(1)){
                        mergeArray(originalList, timeInterval1, timeInterval2);
                    }else{
                        mergeArray(originalList, timeInterval2, timeInterval1);
                    } 
                }                
            }
        }
        
        return originalList;
    }    
        
        public static List<List<Integer>>  mergeArray(List<List<Integer>> originalList,  List<Integer> timeInterval1, List<Integer> timeInterval2 ){
                    if(timeInterval1.get(0)<=timeInterval2.get(1)){
                            Integer startInterval = timeInterval1.get(0) > timeInterval2.get(0) ? timeInterval1.get(0) : timeInterval2.get(0);
                            Integer endInterval = timeInterval1.get(1);
                            
                            List<Integer> tempList = new ArrayList();
                            tempList.add(startInterval);
                            tempList.add(endInterval);

                            originalList.remove(timeInterval1);
                            originalList.remove(timeInterval2);
                        
                            originalList.add(tempList);

                        }
            
            return originalList;
        }
        	
	
}
