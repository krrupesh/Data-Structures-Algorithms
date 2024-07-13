package bit.flip;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//https://www.geeksforgeeks.org/count-minimum-right-flips-to-set-all-values-in-an-array/
/*
 N light bulbs are connected by a wire. Each bulb has a switch associated with it, however due to faulty wiring, a switch also changes the state of all the bulbs to the right of 
 current bulb. Given an initial state of all bulbs, find the minimum number of switches you have to press to turn on all the bulbs. "0 represents the bulb is off and 1 represents 
 the bulb is on."
 
Example:
Input:
2
4
0 0 0 0
4
1 0 0 1
Output:
1
2
 
 
 */
public class FaultyWiringAndBulbs {

	public static void main(String[] args) {

//		int []arr = {0,1,0,1};
//		System.out.println(findMinimumSwitchesToBePressed(arr));	
		
		Scanner sc = new Scanner(System.in);
		int noOfTestCases = sc.nextInt();
		
		System.out.println("noOfTestCases->"+noOfTestCases);
		
		for(int i=0;i<noOfTestCases;i++){
			int noOfBulbs = sc.nextInt();
			
			System.out.println("noOfBulbs->"+noOfBulbs);
			
			List<String> bulbStatesList = new ArrayList<>();
			
			for(int j=0;j<noOfBulbs;j++){
				bulbStatesList.add(sc.next());
				System.out.println("bulbStatesList->"+bulbStatesList);
			}
			System.out.println(findMinimumSwitchesToBePressed(bulbStatesList));
		}
		
	}
	
	public static int findMinimumSwitchesToBePressed(List<String> bulbStatesList){
		int count = 0;
		
		for(String arr : bulbStatesList){
			if(count %2 == 0 && arr.equals("1")){
				continue;
			}else if(count %2 != 0 && arr.equals("0")){
				continue;
			}else if(count %2 == 0 && arr.equals("0")){
				count++;
			}else if(count %2 != 0 && arr.equals("1")){
				count++;
			}
		}
		
	return count;	
	}

}
