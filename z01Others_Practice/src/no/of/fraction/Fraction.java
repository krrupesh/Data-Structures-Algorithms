package no.of.fraction;

import java.util.Scanner;

public class Fraction {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		int n = Integer.parseInt(scanner.nextLine());
		System.out.println("No of fractions = "+findCount(n));
	}
	
	public static int findCount(int n){
		int count = 0;
		
		for(int i = 1;i<=n; i++){
			for(int j = 1;j<=n; j++){

				int N = j;
				int D = i;
				
				if(N<D){
					boolean divigible = false;
					
					if(N != 1){
						for(int k=N; k>1; k--){
							if( N%k ==0 && D%k ==0){
								divigible = true;
								break;
							}
						}
						
						if(!divigible){
							System.out.println(N+"/"+D);
							count++;
						}
					}else{
						System.out.println(N+"/"+D);
						count++;
					}
				}
				
			}
		}
		
		return count;
	}

}
