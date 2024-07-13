package lec14;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Unit1ExerciseSolutionJava8 {

	public static void main(String[] args) {

		List<Person> people = Arrays.asList(
				new Person("Charles","Dickens",60),
				new Person("Lewis","Carroll",42),
				new Person("Thomas","Carlyle",51),
				new Person("Charlotte","Bronte",45),
				new Person("Matthew","Arnold",39));
		

		// Step 1: Sort list by last name
		Collections.sort(people,(p1,p2)-> p1.getLastName().compareTo(p2.getLastName())); // revise Type inference

		

		
		// Step 2: Create the method that prints all the elements in the list
		
		
		
		
		// Step 3: Create a method that prints all people that have last name beginning with C
		printConditionally(people,p->p.getLastName().startsWith("C"));

		
		printConditionally(people,p->p.getFirstName().startsWith("C"));

		
		
		
	}

	public static void printConditionally(List<Person> people, Condition condition){
		for(Person p : people){
			if(condition.test(p)){
				System.out.println(p);
			}
		}
	}
	
}

interface Condition{
	public boolean test(Person p);
}