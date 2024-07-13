package sept07.geeks.arrays;

import java.util.HashSet;
import java.util.Set;

public class CheckSubArraySumIsZero {

	public static void main(String[] args) {

		int arr[] = {10,9,4,-2,-3,-8,12};
		
		checkSubArraySumIsZero(arr);
		
		int arr1[] = {10,9,4,-2,-3,-8,2,12};

		checkSubArraySumIsSomeGivenNumber(arr1, 2);
	}
	
	public static void checkSubArraySumIsZero(int arr[]){
		int prefix_Sum[] = new int[arr.length];
		prefix_Sum[0] = arr[0];
		Set<Integer> hashSet = new HashSet<>();
		hashSet.add(arr[0]);
		
		for(int i=1;i<arr.length;i++){
			prefix_Sum[i] = prefix_Sum[i-1] + arr[i];
			
			System.out.println("prefix_Sum["+i+"]->"+prefix_Sum[i]);

			
			if(!hashSet.contains(prefix_Sum[i])){
				hashSet.add(prefix_Sum[i]);
			}else{
				System.out.println("True");
				break;
			}
		}
	}
	
	/**
	 1) kuchh zero element wala condition tha check that



	 */
	
	public static void checkSubArraySumIsSomeGivenNumber(int arr[], int sum){
		int prefix_Sum[] = new int[arr.length];
		prefix_Sum[0] = arr[0];
		Set<Integer> hashSet = new HashSet<>();
		hashSet.add(arr[0]);
		
		for(int i=1;i<arr.length;i++){
			prefix_Sum[i] = prefix_Sum[i-1] + arr[i];
			
			System.out.println("prefix_Sum["+i+"]->"+prefix_Sum[i]);
			
			if(!hashSet.contains(prefix_Sum[i]-sum)){
				hashSet.add(prefix_Sum[i]);
			}else{
				System.out.println("True");
				break;
			}
		}
	}

}
