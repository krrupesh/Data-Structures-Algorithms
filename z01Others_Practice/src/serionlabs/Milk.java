package serionlabs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Milk {

	public static void main(String[] args) throws IOException {

		//int n = 20;
		//int k = 4;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());
		int k = Integer.parseInt(br.readLine());
		
		int r = 0;
		
		int m = n;
		
		do{
			r = m/k;
			n = n + r;
			m = r;
		}while(r>k);
		
		System.out.println(n);
	}

}
