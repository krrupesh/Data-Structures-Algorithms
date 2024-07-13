package gorgian.conjugates;

import java.util.HashMap;
import java.util.Map;

public class GorgianConjugates {

	public static void main(String[] args) {

		findPrimeNos(1000);
		
		System.out.println();
		
		sumOfSquares(1000);
		
	}

	
	public static void findPrimeNos(int n){
		
		for(int i=2;i<n;i++){
			boolean isPrime = true;
			for(int j=2;j<=i;j++){
				if((j != i) && (i%j == 0)){
					isPrime = false;
					break;
				}
			}
			
			if(isPrime){
				System.out.print(i+" ");
			}
		}
	}
	
	
	public static void sumOfSquares(int m){
		
		for(int n=0;n<m; n++){
			
			int squareRoot = (int) Math.sqrt(n);
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			
			for(int i = 0;i <= squareRoot;i++){
				int firstSum = (i+1)*(i+1);
				map.put(firstSum, firstSum);
				
				if(map.containsKey((n - firstSum))){
					System.out.println("First no : "+(int)Math.sqrt(firstSum)+" Second No "+(int)Math.sqrt(n-firstSum)+" n : "+n);
					//System.out.print(n+" ");
				}
			}
		}
		
		
	}
}
