package prime.product.array;

import java.util.ArrayList;

//return an array containing prime numbers whose product is x
public class PrimeProductArray {

	private static ArrayList<Integer> primeList = new ArrayList<Integer>();//{2,3,5,7,11,13,17,19};
	private static int i = 0;
	//private static int q = 0;
	
	static{
		primeList.add(2);
		primeList.add(3);
		primeList.add(5);
		primeList.add(7);
		primeList.add(11);
		primeList.add(13);
		primeList.add(17);
	}

	final static int x = 30;
	public static void main(String[] args) {

		findProductArray(x);
		
	}
	
	 
	public static void findProductArray(int q){
		
		if(q < 2 || q > x){
			return;
		}
		
		//System.out.println("q="+q+" , Prime No : "+primeList.get(i));
		if(q % primeList.get(i) == 0){
			q = q/primeList.get(i);
			System.out.println(primeList.get(i)+", Step 1 q="+q);
			if(primeList.contains(q)){
				q = q/ primeList.get(++i);
				System.out.println(primeList.get(i)+", Step 2, q="+q);
			}else{
				
				if(q < 2 || q > x ){
					return;
				}
				
				while(q % primeList.get(i) == 0){
					q = q/primeList.get(i);
					System.out.println(primeList.get(i)+", Step 3 q="+q);
				}
				
				i++;
				findProductArray(q);

			}
		}else{
			System.out.println(primeList.get(i)+", Step 4 q="+q);

			if(q % primeList.get(++i) == 0){
				System.out.println(primeList.get(i)+", Step 5 q="+q);
				findProductArray(q);

			}
		} 		
		
	}

}
