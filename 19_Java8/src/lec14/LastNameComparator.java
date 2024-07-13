package lec14;

import java.util.Comparator;

public class LastNameComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {

		Person person1 = (Person)o1;
		Person person2 = (Person)o2;
		
		return person1.lastName.compareTo(person2.lastName);
	}


}
