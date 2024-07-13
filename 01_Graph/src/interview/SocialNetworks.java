package interview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SocialNetworks {

	public static void main(String[] args) {

		List<Integer> counts = new ArrayList<Integer>();
		/*counts.add(3);
		counts.add(3);
		counts.add(3);
		counts.add(3);
		counts.add(3);
		counts.add(1);
		counts.add(3);*/
		counts.add(4);
		counts.add(2);
		counts.add(2);
		counts.add(2);
		counts.add(2);


		socialGroups(counts);
		
	}
	
	
	public static void socialGroups(List<Integer> counts) {
		
		Map<Integer,List> map = new LinkedHashMap();
		
		int i = 0;
		for(Integer key : counts){
			
			if(map.containsKey(key)){
				map.get(key).add(i++);
			}else{
				List list = new ArrayList();
				 list.add(i++);
				map.put(key, list);
			}			
		}
		 System.out.println(map);
		 
		 for(Map.Entry<Integer,List> entry : map.entrySet()){
			 
			 List<Integer> subLists = entry.getValue();
			 
			 int j = 0;
			 for(Integer value : subLists){
				 System.out.print(value+" ");
				 j++;
				 if(j%entry.getKey() == 0){
					 System.out.println();
				 }
			 }
			 
		 }
		
		
		
	}

}
