package intu;

import java.util.*;

public class sortlist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

 
		Set set = new HashSet();
		set.add(1);
		set.add(2);
		set.add(9);
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(1);
		set.add(4);
		set.add(5);
		set.add(7);
		
		List list = new LinkedList();
		
		for(Object s:set){
			int a = (int) s;
			list.add(a);
		}
		
		Collections.sort(list);
		
		System.out.println(list);
		
		
	}

}
