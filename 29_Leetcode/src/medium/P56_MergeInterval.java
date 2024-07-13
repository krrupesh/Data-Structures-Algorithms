package medium;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class P56_MergeInterval {

	public static void main(String[] args) {

		// int [][]intervals = {{1,3},{2,6},{8,10},{15,18}};
		
		int [][]intervals = {{1,4},{2,3}};	
		
		// Output: [[1,6],[8,10],[15,18]]
		
		int [][] merged = mergeInterval(intervals);
		
	}
	
	public static int [][] mergeInterval(int intervals[][]){
		
		Arrays.sort(intervals,(a,b)->Integer.compare(a[0], b[0]));
		
		// declare list of 2D array
		LinkedList<int []> list = new LinkedList();
		
		// add first element in list
		
		list.add(intervals[0]);
		for(int i=1;i<intervals.length;i++){
			int lastElement[] = list.getLast();
			
			if(lastElement[1] >= intervals[i][0]){
				lastElement[1] = lastElement[1] > intervals[i][1] ? lastElement[1] : intervals[i][1];
			}else{
				list.add(intervals[i]);
			}
			printList(list);

		}
		
		printList(list);
		
		return list.toArray(new int[list.size()][2]);
	}
	
	public static void printList(LinkedList<int []> list){
		System.out.println();
		for(int []x : list){
			System.out.print("["+x[0]+","+x[1]+"],");
		}
	}

}
