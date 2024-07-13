package oreilly.generics;

public class MaximumTest {

	public static void main(String[] args) {
		
		System.out.println("Max of Integers : "+maximum(10,5, 20));
		System.out.println("Max of Doubles : "+maximum(10.5,5.6, 20.7));
		System.out.println("Max of Strings : "+maximum("orange","apple", "kiwi"));
		
	}
	
	public static <T extends Comparable<T>> T maximum(T x, T y, T z){
		T max = x;
		
		if(y.compareTo(max) > 0){
			max = y;
		}
		
		if(z.compareTo(max) > 0){
			max = z;
		}
		
		return max;
	}

}
