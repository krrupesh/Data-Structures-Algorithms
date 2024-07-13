package streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsAllMethods {

	private static List<Employee> empList;
	
	private static Employee[] arrayOfEmps = {
		    new Employee(1, "Jeff Bezos", 100000.0), 
		    new Employee(2, "Bill Gates", 200000.0), 
		    new Employee(3, "Mark Zuckerberg", 300000.0)
	};
	
	public static void main(String[] args) {
				
		empList = Arrays.asList(arrayOfEmps);
		empList.stream();
		
		//whenIncrementSalaryForEachEmployee_thenApplyNewSalary();
		
		//whenMapIdToEmployees_thenGetEmployeeStream();
		
		//whenFilterEmployees_thenGetFilteredStream();
		
		//whenFindFirst_thenGetFirstEmployeeInStream();
		
		//whenStreamToArray_thenGetArray();
		
		//whenFlatMapEmployeeNames_thenGetNameStream();
		
		//peekTest();
		
		whenLimitInfiniteStream_thenGetFiniteElements();
	}
	
	

	
	
	public static void whenIncrementSalaryForEachEmployee_thenApplyNewSalary() {    
	    empList.stream().forEach(e -> e.setSalary(e.getSalary() + 100));
	   
	    empList.stream().forEach(System.out::println);
	}
	
	/**
	 * map() produces a new stream after applying a function to each element of the original stream. 
	 * The new stream could be of different type.
	 * The following example converts the stream of Integers into the stream of Employees:
	 */
	public static void whenMapIdToEmployees_thenGetEmployeeStream() {
	    Integer[] empIds = { 1, 2, 3 };
	    
	    List<Employee> employees = Stream.of(empIds)
	      .map(id->arrayOfEmps[id-1])
	      .collect(Collectors.toList());
	    
	    employees.stream().forEach(System.out::println);    
	}
	
	/**
	 * collect is one of the common ways to get stuff out of the stream once we are done with all the processing:
	 */
	public static void whenCollectStreamToList_thenGetList() {
	    List<Employee> employees = empList.stream().collect(Collectors.toList());
	    
	    employees.stream().forEach(System.out::println);    
	}
	
	/**
	 * filter() produces a new stream that contains elements of the original stream 
	 * that pass a given test (specified by a Predicate).
	 */
	public static void whenFilterEmployees_thenGetFilteredStream() {
	    Integer[] empIds = { 1, 2, 3};
	    
	    List<Employee> employees = Stream.of(empIds)
	      .map(id->arrayOfEmps[id-1])
	      .filter(e -> e != null) // argument of filter is called predicate 
	      .filter(e -> e.getSalary() > 200000)
	      .collect(Collectors.toList());
	    
	    employees.stream().forEach(System.out::println);    
	}
	
	/**
	 * findFirst() returns an Optional for the first entry in the stream
	 */
	public static void whenFindFirst_thenGetFirstEmployeeInStream() {
	    Integer[] empIds = { 1, 2, 3};
	    
	    Employee employee = Stream.of(empIds)
	      .map(id->arrayOfEmps[id-1])
	      .filter(e -> e != null)
	      .filter(e -> e.getSalary() > 100000)
	      .findFirst()
	      .orElse(null);
	    
	    System.out.println(employee);    
	}
	
	/**
	 * If we need to get an array out of the stream, we can simply use toArray():
	 * The syntax Employee[]::new creates an empty array of Employee – 
	 * which is then filled with elements from the stream.
	 */
	public static void whenStreamToArray_thenGetArray() {
	    Employee[] employees = empList.stream().toArray(Employee[]::new);

	    Stream.of(employees).forEach(System.out::println);
	}
	
	/**
	 * A stream can hold complex data structures like Stream<List<String>>. 
	 * In cases like this, flatMap() helps us to flatten the data structure to simplify further operations
	 * 
	 * Notice how we were able to convert the Stream<List<String>> to a simpler Stream<String> – using the flatMap() API
	 */
	public static void whenFlatMapEmployeeNames_thenGetNameStream() {
	    List<List<String>> namesNested = Arrays.asList( 
	      Arrays.asList("Jeff", "Bezos"), 
	      Arrays.asList("Bill", "Gates"), 
	      Arrays.asList("Mark", "Zuckerberg"));

	    List<String> namesFlatStream = namesNested.stream()
	      .flatMap(Collection::stream)
	      .collect(Collectors.toList());

	    namesFlatStream.stream().forEach(System.out::println);    
	}
		
	/**
	 * peek() is an intermediate operation
	 * We saw forEach() earlier in this section, which is a terminal operation. However, sometimes we need to perform 
	 * multiple operations on each element of the stream before any terminal operation is applied.

	   peek() can be useful in situations like this. Simply put, it performs the specified operation on 
	   each element of the stream and returns a new stream which can be used further.
	 */
	public static void peekTest() {
	    empList.stream()
	      .peek(e -> e.setSalary(e.getSalary() + 100))
	      .peek(System.out::println)
	      .collect(Collectors.toList());
	}
	
	/**
	 * Here’s a sample stream pipeline, where empList is the source, 
	 * filter() is the intermediate operation and count is the terminal operation:
	 */
	public static void streamPipeline() {
	    Long empCount = empList.stream()
	      .filter(e -> e.getSalary() > 200000)
	      .count();

	    System.out.println(empCount);
	}
	
	
	

	/**
	 * Some operations are deemed short-circuiting operations. 
	 * Short-circuiting operations allow computations on infinite streams to complete in finite time:
	 * 
	 * Here, we use short-circuiting operations skip() to skip first 3 elements, 
	 * and limit() to limit to 5 elements from the infinite stream generated using iterate().
	 */
	public static void whenLimitInfiniteStream_thenGetFiniteElements() {
	    Stream<Integer> infiniteStream = Stream.iterate(2, i -> i * 2);

	    List<Integer> collect = infiniteStream
	      .skip(3)
	      .limit(5)
	      .collect(Collectors.toList());
	    System.out.println(collect);
	}
	
	
	//// Lazy initialization 


	/**
	 * For example, consider the findFirst() example we saw earlier. How many times is the map() 
	 * operation performed here? 4 times, since the input array contains 4 elements?
	 * 
	 * Stream performs the map and two filter operations, one element at a time.
	 */
	public static void findFirstLazy() {
	    Integer[] empIds = { 1, 2, 3};
	    
	    Employee employee = Stream.of(empIds)
	      .map(id->arrayOfEmps[id-1])
	      .filter(e -> e != null)
	      .filter(e -> e.getSalary() > 100000)
	      .findFirst()
	      .orElse(null);
	    
	    System.out.println(employee.getSalary());
	}
			
	
	/// Comparison Based Stream Operations
	
	
	/**
	 * Note that short-circuiting will not be applied for sorted().
	 * even if we had used findFirst() after the sorted(), the sorting of all 
	 * the elements is done before applying the findFirst().
	 */
	public void whenSortStream_thenGetSortedStream() {
	    List<Employee> employees = empList.stream()
	      .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
	      .collect(Collectors.toList());
	    System.out.println(employees.toString());
	}
	
	
	/**
	 * 
	 */
	public static void whenFindMin_thenGetMinElementFromStream() {
	    Employee firstEmp = empList.stream()
	      .min((e1, e2) -> e1.getId() - e2.getId())
	      .orElseThrow(NoSuchElementException::new);
	    System.out.println(firstEmp);
	}
	
	
	
	public static void distinctTest() {
	    List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
	    List<Integer> distinctIntList = intList.stream().distinct().collect(Collectors.toList());
	    
	    System.out.println(distinctIntList);
	}
	
	
	
	

	/**
	 * allMatch, anyMatch, and noneMatch
	 * 
	 * These operations all take a predicate and return a boolean. Short-circuiting is applied 
	 * and processing is stopped as soon as the answer is determined:
	 */
	public void whenApplyMatch_thenReturnBoolean() {
	    List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);
	    
	    boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
	    boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
	    boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);
	    
	    System.out.println(allEven == false);
	    System.out.println(oneEven == true);
	    System.out.println(noneMultipleOfThree == false);
	}
	
	
	

	/**
	 * toCollection
	 * 
	 * 	We can use Collectors.toCollection() to extract the elements into any other collection by 
	 * passing in a Supplier<Collection>. We can also use a constructor reference for the Supplier:
	 */
	public void toCollectionTest() {
	    Vector<String> empNames = empList.stream()
	            .map(Employee::getName)
	            .collect(Collectors.toCollection(Vector::new));
	    
	    System.out.println(empNames);
	}
	
	//https://www.java-success.com/java-8-string-streams-finding-first-non-repeated-character-functional-programming/
	private static Character findFirstUniqueCharacter(String input) {  
        Character result =  input.chars()           // IntStream
             .mapToObj(i -> Character.toLowerCase(Character.valueOf((char) i)))  // convert to lowercase & then to Character object Stream
             .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())) // store in a LinkedHashMap with the count
             .entrySet().stream()                       // EntrySet stream
             .filter(entry -> entry.getValue() == 1L)   // extracts characters with a count of 1
             .map(entry -> entry.getKey())              // get the keys of EntrySet
             .findFirst().get();                        // get the first entry from the keys
        
         return result;    
  }


}
