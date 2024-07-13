package ola;

import java.util.Scanner;

class GFG {
	public static void main (String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int sum = 0;
		int num = Integer.parseInt(sc.nextLine());

		while(sc.hasNext()){
		    int a = Integer.parseInt(sc.nextLine());
		    
		    sum = sum + a;
		}
		
		System.out.println(sum/num);
	}
}