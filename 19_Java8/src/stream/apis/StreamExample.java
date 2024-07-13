package stream.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StreamExample obj = new StreamExample();
		
		//obj.flatMap();
		
		//obj.distinct();
		
		
	// Terminal operations
		//obj.count();
		//obj.anyMatch();
		//obj.allMatch();
		//obj.noneMatch();
		//obj.collect();
		obj.reduce();

	}

	public void flatMap() {

		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");

		Stream<String> stream = stringList.stream();

		stream.flatMap((value) -> {
			String[] split = value.split(" ");
			return (Stream<String>) Arrays.asList(split).stream();
			
		}).forEach((value) -> System.out.println(value));

	}
	
	public void distinct(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("one");
		stringList.add("two");
		stringList.add("three");
		stringList.add("one");

		Stream<String> stream = stringList.stream();

		List<String> distinctStrings = stream
		        .distinct()
		        .collect(Collectors.toList());

		System.out.println(distinctStrings);
	}
	
	public void limit(){
		
	}

	
	
	// terminal operations
	public void count(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");
		
		long count = stringList.stream()
				  .map((value) -> { return value.toLowerCase(); })
				  .map((value) -> { return value.toUpperCase(); })
				  .map((value) -> { return value.substring(0,3); })
				  .count();
		
		System.out.println(count);
	}
	
	
	// terminal operations
	public void anyMatch(){
		
		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");

		Stream<String> stream = stringList.stream();

		boolean anyMatch = stream.anyMatch((value) -> { return value.startsWith("One"); });
		System.out.println(anyMatch);	
	}
	
	// terminal operations
	public void allMatch(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");

		Stream<String> stream = stringList.stream();

		boolean allMatch = stream.allMatch((value) -> { return value.startsWith("One"); });
		System.out.println(allMatch);	

	}
	
	public void noneMatch(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("abc");
		stringList.add("def");

		Stream<String> stream = stringList.stream();

		boolean noneMatch = stream.noneMatch((element) -> {
		    return "xyz".equals(element);
		});

		System.out.println("noneMatch = " + noneMatch);
	}
	
	// how to collect in a map
	public void collect(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");

		Stream<String> stream = stringList.stream();

		List<String> stringsAsUppercaseList = stream
		.map(value -> value.toUpperCase())
		.collect(Collectors.toList());
		
		System.out.println(stringsAsUppercaseList);
	}
	
	public void reduce(){
		List<String> stringList = new ArrayList<String>();

		stringList.add("One flew over the cuckoo's nest");
		stringList.add("To kill a muckingbird");
		stringList.add("Gone with the wind");

		Stream<String> stream = stringList.stream();

		Optional<String> reduced = stream.reduce((value, combinedValue) -> {
		    return combinedValue + " + " + value;
		});

		System.out.println(reduced.get());
	}
	
	
}
