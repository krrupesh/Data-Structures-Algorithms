import java.util.HashMap;
import java.util.*;
import java.util.Map;

// Map ---> List<Map> ---> Collections.sort() --> List<Map> (Sorted) ---> LinkedHashMap

public class SortByValueExample1 {

    public static void main(String[] args) {

        Map<String, Integer> unsortMap = new HashMap<String, Integer>();
        unsortMap.put("z", 10);
        unsortMap.put("b", 5);
        unsortMap.put("a", 6);
        unsortMap.put("c", 20);
        unsortMap.put("d", 1);
/*        unsortMap.put("e", 7);
        unsortMap.put("y", 8);
        unsortMap.put("n", 99);
        unsortMap.put("j", 50);
        unsortMap.put("m", 2);
        unsortMap.put("f", 9);*/

        System.out.println("Unsort Map......");
        printMap(unsortMap);

        System.out.println("\nSorted Map......By Value");
        Map<String, Integer> sortedMap = sortByValue(unsortMap);
        printMap(sortedMap);

    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

    
    //4. Another Way of Using Generic Types

    /*public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
    	List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    	Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
    		@Override
    		public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
    			return (e1.getValue()).compareTo(e2.getValue());
    		}
    	});
     
    	Map<K, V> result = new LinkedHashMap<>();
    	for (Map.Entry<K, V> entry : list) {
    		result.put(entry.getKey(), entry.getValue());
    	}
     
    	return result;
    }*/
}