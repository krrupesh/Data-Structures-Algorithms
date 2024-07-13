package oreilly.lambdas.streams;

import java.util.stream.IntStream;

public class IntStreamOperations {

	public static void main(String[] args) {

		int []values = {3,10,6,1,4,8,2,5,9,7};
		
		System.out.println("Original values");
		
		IntStream.of(values).forEach(value -> System.out.print(value + " "));
		
		System.out.println();
		
		System.out.println("Count: "+IntStream.of(values).count());
		
		System.out.println("Min: "+IntStream.of(values).min().getAsInt());

		System.out.println("Max: "+IntStream.of(values).max().getAsInt());

		System.out.println("Sum: "+IntStream.of(values).sum());
		
		System.out.println("Sum via reduce method: "+
				IntStream.of(values)
							.reduce(0,(x,y) -> x+y));
		
		System.out.println("Sum of squares via reduce method: "+
				IntStream.of(values)
							.reduce(0,(x,y) -> x+ y*y));
		
		System.out.println("Product via reduce method: "+
				IntStream.of(values)
							.reduce(1,(x,y) -> x*y));
		
		System.out.println("Even values displayed in sorted order: ");
		IntStream.of(values)
				.filter(value -> value % 2 == 0)
				.sorted()
				.forEach(value -> System.out.print(value+ " "));

		System.out.println("Odd values multiplied by 10 displayed in sorted order: ");
		IntStream.of(values)
				.filter(value -> value % 2 != 0)
				.map(value -> value*10)
				.sorted()
				.forEach(value -> System.out.print(value+ " "));
		
		
		
		
		
	}

}
