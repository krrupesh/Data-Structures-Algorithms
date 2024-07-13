package com.journaldev.generics;

import java.util.ArrayList;
import java.util.List;

public class GenericsWildcards {

	public static void main(String[] args) {
		List<Integer> ints = new ArrayList<>();
		ints.add(3); ints.add(5); ints.add(10);
		double sum = sum(ints);
		System.out.println("Sum of ints="+sum);
	}

	public static double sum(List<? extends Number> list){ // List<? extends Number>
		double sum = 0;
		for(Number n : list){
			sum += n.doubleValue();
			
			//list.add(20); why we can'nt add into this list
		}
		return sum;
	}
}