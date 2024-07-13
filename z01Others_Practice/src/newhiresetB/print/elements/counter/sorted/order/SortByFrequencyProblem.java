package newhiresetB.print.elements.counter.sorted.order;
import java.util.*;
import java.util.Map.Entry;

public class SortByFrequencyProblem {
 
    private static int [] sortByFrequency(int[] arr) {
        Map<Integer, Integer> frequencyMap = createFrequencyMap(arr);
 
        List<java.util.Map.Entry<Integer, Integer>> entryList = sortByValue(frequencyMap);
 
       return  putSortedElementsBackInArray(arr, entryList);
    }
 
    private static Map<Integer, Integer> createFrequencyMap(int[] arr) {
 
        // Use LinkedHashMap because it maintains insertion order of elements.
        Map<Integer, Integer> frequencyMap = new LinkedHashMap<>();
 
        for (int i = 0; i < arr.length; i++) {
            int key = arr[i];
            if (frequencyMap.containsKey(key)) {
                frequencyMap.put(key, frequencyMap.get(key) + 1);
            } else {
                frequencyMap.put(key, 1);
            }
        }
        return frequencyMap;
    }
 
    private static List<java.util.Map.Entry<Integer, Integer>> sortByValue(
            Map<Integer, Integer> frequencyMap) {
 
        // List containing elements of map's entry set.
        List<java.util.Map.Entry<Integer, Integer>> entryList = new ArrayList<java.util.Map.Entry<Integer, Integer>>(
                frequencyMap.entrySet());
 
        // Sort the list.
        Collections.sort(entryList,
                new Comparator<java.util.Map.Entry<Integer, Integer>>() {
 
                    @Override
                    public int compare(java.util.Map.Entry<Integer, Integer> o1,
                    		java.util.Map.Entry<Integer, Integer> o2) {
                        return compareIt(o1,o2);
                    }
                });
        return entryList;
    }
    
    public static int compareIt(java.util.Map.Entry<Integer, Integer> o1, java.util.Map.Entry<Integer, Integer> o2){
    	
    	if((o2.getValue().compareTo(o1.getValue())) == 0){
    		return o1.getKey().compareTo(o2.getKey());
    	}else{
    		return o2.getValue().compareTo(o1.getValue());
    	}
    	
    }
 
    public static int[] putSortedElementsBackInArray(int[] arr,
            List<Entry<Integer, Integer>> list) {
        int index = 0;
 
        // Arrange array elements in sorted list of entry set of frequency map.
        for (java.util.Map.Entry<Integer, Integer> entry : list) {
            for (int i = 0; i < entry.getValue(); i++) {
                arr[index++] = entry.getKey();
            }
        }
        
        return arr;
    }
 
    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
 
    public static void main(String[] args) {
        int[] arr = { 2, 5, 3, 8, 7, 2, 5, 2, 3, 3, 5, 3 };
 
        System.out.println("Input array before sorting elements by frequency.");
        printArray(arr);
 
        sortByFrequency(arr);
 
        System.out.println();
        System.out.println();
 
        System.out.println("Array after sorting elements by frequency.");
        printArray(arr);
    }
}