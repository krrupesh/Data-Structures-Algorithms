package newhiresetB.print.elements.counter.sorted.order;

import java.util.*;
public class SortArray {

	/**
	 * @param args
	 */
	//Output: {2,2,2,1,1,3,4,8}

	public static void main(String[] args) {
		int a[]={2,3,4,2,8,1,1,2};
		Map hm=new HashMap<>();
		int count=1;
		for (int i = 0; i < a.length; i++) {
			if(hm.containsKey(a[i])){
				int c=(int)hm.get(a[i]);			
				++c;
				hm.put(a[i], c);
			}
			else
				hm.put(a[i], count);
		}
		System.out.println(hm);
		
		Set s=hm.entrySet();
		
		Iterator it=s.iterator();
		
		Map<Integer ,List> m1=new HashMap<Integer,List>();
		while(it.hasNext()){
			Map.Entry me=(Map.Entry)it.next();
			int key=(int)me.getKey();
			int val=(int)me.getValue();
			
			
			if(m1.containsKey(val)){
				List l2=(List)m1.get(val);
				l2.add(key);
				m1.put(val, l2);
			}
			else{
			List l1=new ArrayList();
			l1.add(key);
			m1.put(val, l1);
			}	
		}
		
		Set s1=m1.keySet();
		Iterator it1=s1.iterator();
		List l3=new ArrayList();
		
		while(it1.hasNext()){
			int value=(int)it1.next();
			l3.add(value);
		}
		
		Collections.sort(l3);
		Collections.reverse(l3);
		System.out.println(l3);
		System.out.println(m1);
		
		Iterator it2=l3.iterator();
		while(it2.hasNext()){
			int co=(int)it2.next();
			List no=null;
			for(int i=0;i<co;i++){
				
				no=(List)m1.get(co);
			   Collections.sort(no);
			   Collections.reverse(no);
               System.out.print(no);
			}
		}

	}

}