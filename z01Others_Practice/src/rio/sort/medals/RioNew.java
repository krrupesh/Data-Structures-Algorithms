/* take input and create object and store it into Map.
map(name , object)
1)you got one Country name, and its position.
2)1st check that country name is existing in HashMap or not.
3)If not put name as key and in object set the coins property.
4)If name exist in Map, then take out the value(country Object) and 
  do set the respective coins by incrementing by 1. And put it into Map.
  
*/

package rio.sort.medals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class RioNew {

	public static void main(String[] args) throws IOException {
		
		System.out.println("ENTER VALUE OF N :");
		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt();
		int i=0;
		Map<String, Country> unsortedReo=new HashMap<String, Country>();
		while(i<n){
			i++;
			System.out.println("Enter country names :");
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String s=br.readLine();
			String str[]=s.split(" ");
			
	// 1ST position for Gold  
			if(unsortedReo.containsKey(str[0])){
				Country c0=new Country();
				c0=unsortedReo.get(str[0]);
				c0.setGold(c0.getGold()+1);
				unsortedReo.put(str[0], c0);
			}else{
				 Country c0=new Country();
				 c0.setGold(1);
				 unsortedReo.put(str[0], c0);			
			}
			
	// 2nd position for Silver		
			if(unsortedReo.containsKey(str[1])){
				Country c1=new Country();
				c1=unsortedReo.get(str[1]);
				c1.setSilver(c1.getSilver()+1);
				unsortedReo.put(str[1], c1);
			}else{
				 Country c1=new Country();
				 c1.setSilver(1);
				 unsortedReo.put(str[1], c1);				
			}
			
	//3rd position for Bronze
			if(unsortedReo.containsKey(str[2])){
				Country c2=new Country();
				c2=unsortedReo.get(str[2]);
				c2.setBronze(c2.getBronze()+1);
				unsortedReo.put(str[2], c2);
			}else{
				 Country c2=new Country();
				 c2.setBronze(1);
				 unsortedReo.put(str[2], c2);				
			}
			
	}
// Map is ready to be sorted And displayed.
		 //System.out.println("Unsort Map......");
	        //printMap(unsortedReo);	
	        //System.out.println("\nSorted Map......By Value");
	        
	        List<Map.Entry<String, Country>> list = sortByValueList(unsortedReo);
	        printList(list);
		
}
	
private static List<Map.Entry<String, Country>> sortByValueList( Map<String, Country> unsortMap) {
	// 1. Convert Map to List of Map
    List<Map.Entry<String, Country>> list=new LinkedList<Map.Entry<String, Country>>(unsortMap.entrySet());
    
    // 2. Sort list with Collections.sort(), provide a custom Comparator
    //  Try switch the o1 o2 position for a different order
    Collections.sort(list, new Comparator<Map.Entry<String, Country>>() {

		@Override
		public int compare(Entry<String, Country> o1, Entry<String, Country> o2) {
			
			if((o1.getValue().getGold()==o2.getValue().getGold()) && (o1.getValue().getSilver()==o2.getValue().getSilver()) && (o1.getValue().getBronze()==o2.getValue().getBronze()))
				return o1.getKey().compareTo(o2.getKey());
			
			else if((o1.getValue().getGold()==o2.getValue().getGold()) && (o1.getValue().getSilver()==o2.getValue().getSilver()))
				return  o2.getValue().getBronze()-o1.getValue().getBronze();
			
			else if(o1.getValue().getGold()==o2.getValue().getGold())
				return  o2.getValue().getSilver()-o1.getValue().getSilver();
			
			else 
				return o2.getValue().getGold()-o1.getValue().getGold();
		}
    });

    return list;
 }
	 
	 public static  void printMap(Map<String, Country> map) {
	        for (Map.Entry<String, Country> entry : map.entrySet()) {
	            System.out.println(entry.getKey()  +"  " + entry.getValue().getGold() + " "+entry.getValue().getSilver() +" "+entry.getValue().getBronze());
	        }
	 }
	 
	 public static  void printList(List<Map.Entry<String, Country>> list) {
	        for (Map.Entry<String, Country> entry : list) {
	            System.out.println(entry.getKey()  +"  " + entry.getValue().getGold() + " "+entry.getValue().getSilver() +" "+entry.getValue().getBronze());
	        }
	 }
}
