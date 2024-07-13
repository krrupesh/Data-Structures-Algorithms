package lec16;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import lec14.Person;

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
		printConditionally(people,p->true,p->System.out.println(p));
		
		
		
		// Step 3: Create a method that prints all people that have last name beginning with C
		printConditionally(people,p->p.getLastName().startsWith("C"),p->System.out.println(p.getLastName()));

		
		printConditionally(people,p->p.getFirstName().startsWith("C"),p->System.out.println(p));
		
	}

	// passing the behaviour in the method
	public static void printConditionally(List<Person> people, Predicate<Person> predicate,Consumer<Person> consumer){
		for(Person p : people){
			if(predicate.test(p)){
				consumer.accept(p);
			}
		}
	}
	
/*	// using standard functional interfaces provided by java out of box, we choose this accoring to signature of lambda expression
	public static void printConditionally(List<Person> people, Predicate<Person> predicate){
		for(Person p : people){
			if(predicate.test(p)){
				System.out.println(p);
			}
		}
	}*/
	
}
