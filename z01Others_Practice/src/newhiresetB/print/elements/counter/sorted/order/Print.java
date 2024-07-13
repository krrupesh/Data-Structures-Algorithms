package newhiresetB.print.elements.counter.sorted.order;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Print {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	
	public static void printOutFrequencySort(int[] a) {
		//Print input.
		for( int i = 0; i < a.length; ++i) {
		System.out.print( a[i] + " ");
		}
		System.out.println();

		Map counting = new HashMap();
		Comparator cp = new KeyValueComparator(counting);
		TreeMap treeMap = new TreeMap(cp);

		for( int i = 0; i < a.length; ++i) {
		Integer value = (Integer) counting.get(a[i]);
		if( value == null) {
		counting.put(a[i], new Integer(1));
		} else {
		counting.put( a[i], new Integer(value + 1));
		}
		}

		System.out.println( counting);
		treeMap.putAll(counting);
		System.out.println( treeMap );

		// print output
		for( Map.Entry entry : treeMap.entrySet()) {
		Integer value = entry.getKey();
		Integer frequency = entry.getValue();
		for( int i = 0; i < frequency.intValue(); ++i) {
		System.out.print(value + " ");
		}
		}

		System.out.println();

		}

		static class KeyValueComparator implements Comparator {

		Map map;
		KeyValueComparator(Map map) {
		this.map = map;
		}

		@Override
		public int compare(Integer k1, Integer k2) {
		Integer v1 = map.get(k1);
		Integer v2 = map.get(k2);
		if( v1 > v2) {
		return -1;
		} else if ( v1 == v2) {
		return k1 < k2 ? -1 : 1;
		}

		return 1;
		}
		}
	
	

}
