package hashcode.equals;

import java.util.HashMap;
import java.util.Map;

public class HashCodeEqualsTest {

	public static void main(String[] args) {

		Employee emp1 = new Employee(28, "Rupesh");
		Employee emp2 = new Employee(28, "Rupesh");
		
		System.out.println("emp1 "+emp1);
		System.out.println("emp2 "+emp2);
				
        System.out.println(emp1 == emp2);
        System.out.println(emp1.equals(emp2));
        
        Map<Employee,Integer> map = new HashMap<Employee,Integer>();
        
        map.put(emp1, 100);
        map.put(emp2, 200);

        
        System.out.println(map);
	}

}
