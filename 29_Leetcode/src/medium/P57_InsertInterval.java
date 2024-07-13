package medium;

public class P57_InsertInterval {

	public static void main(String[] args) {

		int [][]intervals = {{1,2},{3,5},{6,7},{8,10},{12,16}};
		int [] newInterval = {4,8};
		
		insert(intervals, newInterval);
	}
	
	// fix this
	 public static int[][] insert(int[][] intervals, int[] newInterval) {
		 
		 int startIndex = 0;
		 boolean startIndexFound = false;
		 int endIndex = 0;
		 boolean endIndexFound = false;
		 for(int i =0; i< intervals.length;i++){
			 
			 if(newInterval[0] <= intervals[i][1] && !startIndexFound){
				 startIndex = i;
				 startIndexFound = true;
			 }
			 
			 if(newInterval[1] <= intervals[i][0] && !endIndexFound){
				 endIndex = i;
				 endIndexFound = true;
			 }
			 
		 }	 
		 
		 if(startIndexFound && startIndexFound){
			 
		 }else if(startIndexFound){
			 
		 }
		 
		 System.out.println("startIndex : ["+intervals[startIndex][0]+","+intervals[startIndex][1]+"]");
		 
		 System.out.println("endIndex : ["+intervals[endIndex][0]+","+intervals[endIndex][1]+"]");

		 
		 // baby steps
		 
		 // 1. add new interval in the begining if its smaller than first element
		 
		 // 2. merge the last element added to list with next elements
		 
		 // 3. once merging is not required then just add next elements to list 
		 
		 // 4. convert list to array
		 
		 
		 
		 int x = 10;
		 int[][] merged = new int[x][2];  
		 
		 return merged;
	 }

}
