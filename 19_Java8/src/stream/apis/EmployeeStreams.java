package stream.apis;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// source 
// https://medium.com/@moiz.mhb/java-8-stream-api-interview-questions-7336f8a17968
public class EmployeeStreams {

	static List<Employee> employeeList; 
	public static void main(String[] args) {
		employeeList = Employee.getEmployeeList();

		//getEmployeeCount();
		
		//sortEmployeeObject();
		
		//sortEmployeeObjectDec();
		
		//countMaleAndFemaleEmplyees();
		
		//countEmplyeesInDepartment();
		
		//getNameOfAllDepartment();
		
		//findAverageSalaryfMaleAndFemaleEmployee();
		
		//fetchHighestPaidMaleAndFemaleEmployee();
		
		getHighestPaidEmployeeInEachDepartment();
		
	}
	
	
	
	
	// 1. get count of employees 
	public static void getEmployeeCount(){
		
		long count = employeeList.stream().count();
		System.out.println("Employee count using count(): "+count);
		
		long count1 = employeeList.stream().collect(Collectors.counting());
		System.out.println("Employee count using Collectors.counting(): "+count1);
	}
	
	
	// 2. sort employee object based on salary in ascending order
	public static void sortEmployeeObject(){
		
		List<Employee> employeeListSorted = employeeList.stream()
		.sorted((o1,o2)-> (int)(o1.getSalary() - o2.getSalary()))
		.collect(Collectors.toList());
		
		System.out.println("Sorted Employee in ascending order : \n"+employeeListSorted);
	}

	// 3. sort employee object based on salary in descending order
	public static void sortEmployeeObjectDec(){
		
		List<Employee> employeeListSorted = employeeList.stream()
		.sorted((o1,o2)-> (int)(o2.getSalary() - o1.getSalary()))
		.collect(Collectors.toList());
		
		System.out.println("Sorted Employee in ascending order : \n"+employeeListSorted);
	}
	
	// 4. how many male and female employees are there in organization
	public static void countMaleAndFemaleEmplyees(){
		Map<String,Long> count = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()));
		
		System.out.println("Male and Female Count : "+count);
	}
	
	// 5. how many employees are there in each department
	public static void countEmplyeesInDepartment(){
		Map<String,Long> count = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
		
		System.out.println("Employees Count in department: "+count);
	}
	
	// 6. how many employees are there in each department
	public static void getNameOfAllDepartment(){
		List<String> count = employeeList.stream()
				.map(Employee::getDepartment)
				.distinct()
				.collect(Collectors.toList());
		
		System.out.println("names of all the department: "+count);
	}
	
	
	// 7. findAverageSalaryfMaleAndFemaleEmployee
	public static void findAverageSalaryfMaleAndFemaleEmployee(){
		Map<String,Double> count =  employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getGender, Collectors.averagingDouble(Employee::getSalary)));
		
		System.out.println("findAverageSalaryfMaleAndFemaleEmployee: "+count);
	}
	
	// 8. Fetch the highest-paid male and female employee
	public static void fetchHighestPaidMaleAndFemaleEmployee(){
		Map<String, Optional<Employee>> maxSalaryMaleAndFemaleEmployee = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getGender, Collectors.maxBy((s1,s2)->(int)(s1.getSalary()-s2.getSalary()))));
		
		System.out.println("maxSalaryMaleAndFemaleEmployee: "+maxSalaryMaleAndFemaleEmployee);
	}
	
	// 9. Fetch the highest-paid male and female employee
	public static void fetchLowestPaidMaleAndFemaleEmployee(){
		Map<String, Optional<Employee>> minSalaryMaleAndFemaleEmployee = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getGender, Collectors.minBy((s1,s2)->(int)(s1.getSalary()-s2.getSalary()))));
		
		System.out.println("minSalaryMaleAndFemaleEmployee: "+minSalaryMaleAndFemaleEmployee);
	}
	
	// 10. Get the highest-paid employee in each department
	public static void getHighestPaidEmployeeInEachDepartment(){
		Map<String, Optional<Employee>> highestPaidEmployeeInEachDepartment = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.maxBy((s1,s2)->(int)(s1.getSalary()-s2.getSalary()))));
		
		System.out.println("maxSalaryMaleAndFemaleEmployee: "+highestPaidEmployeeInEachDepartment);
	}
	
}
