package Map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HasMapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map hm= new HashMap();
		hm.put(1, 100);
		hm.put(2, 200);
		hm.put(3, 300);
		hm.put(4, 400);
		hm.put(5, 500);
		
		Set s = hm.entrySet();
		Iterator it = s.iterator();
		
		while(it.hasNext()){
			Map.Entry me=(Map.Entry) it.next();
			
		System.out.println(me.getKey()  +"  ====>"+me.getValue());
		}
		
	}

}
