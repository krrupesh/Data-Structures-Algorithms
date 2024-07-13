package spring.circular.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircularDependencyCheck {

	static Map <String,String> beanStatus = new HashMap<String,String>(); 
	static Map <String,List<String>> dependentClasses = new HashMap <String,List<String>>();

	public static void main(String[] args) {

		// A ->B,C,D
		// B ->E,F
		// C ->G,A	
		
		List <String> alist = new ArrayList<String>();
		alist.add("B");
		alist.add("C");
		alist.add("D");
		
		dependentClasses.put("A", alist);
		
		List <String> blist = new ArrayList<String>();
		alist.add("E");
		alist.add("F");
		
		dependentClasses.put("B", blist);
		
		List <String> clist = new ArrayList<String>();
		alist.add("G");
		alist.add("A");
		
		dependentClasses.put("C", clist);
		
		dependentClasses.put("D", null);
		dependentClasses.put("E", null);
		dependentClasses.put("F", null);
		dependentClasses.put("G", null);
		//dependentClasses.put("H", null);

	
		boolean created = checkCircularDependency(dependentClasses);	
		System.out.println(created);
	}
	
	public static boolean checkCircularDependency(Map <String, List<String>> beansDetails){
		boolean created = false;	
		for (Map.Entry<String, List<String>> entry : beansDetails.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			System.out.println("key : "+key+",value : "+value);
			
			if((beanStatus.get(key) != null) && beanStatus.get(key).equals("InProgress")){
				System.out.println("Failed at bean : "+key);
				return false;
			}	
			
			beanStatus.put(key, "InProgress");
			
			if((value != null) && (created = isBeanCreated(value))){
				beanStatus.put(key, "Created");
			}
		}	
		
		return created;
	}
	
	public static boolean isBeanCreated(List<String> beansList){
			
		for(String bean:beansList){
			
			if((beanStatus.get(bean) != null) && beanStatus.get(bean).equals("InProgress")){
				System.out.println("Failed at bean : "+bean);
				return false;
			}
			
			beanStatus.put(bean, "InProgress");
			List<String> value = dependentClasses.get(bean);
			System.out.println("bean : "+bean+",value : "+value);
			System.out.println(beanStatus);
			
			if((value != null) && (isBeanCreated(value))){
				beanStatus.put(bean, "Created");
			}			
		}
		return true;
	}
}
