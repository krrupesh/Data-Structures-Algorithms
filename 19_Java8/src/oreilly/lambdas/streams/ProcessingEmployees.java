package oreilly.lambdas.streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProcessingEmployees {

	public static void main(String[] args) {

		Employee[] employees = {
				new Employee("Rupesh", "kumar", 3000, "IT"),
				new Employee("cRupesh1", "kumar1", 3002, "CS"),
				new Employee("Rupesh2", "ckumar2", 5005, "IT"),
				new Employee("bRupesh3", "bkumar3", 5045, "CS"),
				new Employee("Rupesh4", "kumar4", 5500, "EC"),
				new Employee("aRupesh5", "akumar5", 6000, "EC")
		};
		
		
		List<Employee> list = Arrays.asList(employees);
		
		list.stream().forEach(System.out::println);
			
		Predicate<Employee> fourToSixThousand = 
				e -> (e.getSalary() >= 4000 && e.getSalary() <= 6000);
			
		System.out.println("Salary greater than 4000 less than 6000");		
		list.stream()
			.filter(fourToSixThousand)
			.sorted(Comparator.comparing(Employee::getSalary))
			.forEach(System.out::println);
		
		System.out.println("first employee with Salary greater than 4000 less than 6000"+
		list.stream()
			.filter(fourToSixThousand)
			.findFirst()
			.get());
		
		Function<Employee, String> byFirstName = Employee::getFirstName;
		Function<Employee, String> byLastName = Employee::getLastName;
		
		// Comparator for comparing employees by first name then last name
		Comparator<Employee> firstThenLast = Comparator.comparing(byFirstName).thenComparing(byLastName);
		
		System.out.println("Employees in ascending order by first name then last name");
		list.stream()
			.sorted(firstThenLast)
			.forEach(System.out::println);
		
		System.out.println("Employees in decscending order by first name then last name");
		list.stream()
		.sorted(firstThenLast.reversed())
		.forEach(System.out::println);
		
		System.out.println("Unique Employees last name");
		list.stream()
			.map(Employee::getLastName)
			.distinct()
			.sorted()
			.forEach(System.out::println);

		
		
		
	}

}
