package level1;
/*
 * find the max difference in the given array
 */
public class Maximum_difference {

	public static void main(String[] args) {

		int arr [] = { 7, 9, 5, 6, 3, 2 };
		
		System.out.println(findmaxdifference(arr));
	}

	/*
	 * initially i was thinking that i will have to find min and max no and then find the its diff, but
	 * 
	 * we can solve it by maintaining two variables 
	 * min_no : it will contain the min no as we move forward
	 * max_diff : as we move forward we can we can update this variable by subtracting min_no from current value
	 */
	public static int findmaxdifference(int arr[]){
		
		int min_no = arr[0];
		int max_diff = arr[1]-arr[0];
		
		for(int i=1;i<arr.length;i++){
			
			if(arr[i] < min_no){
				min_no = arr[i];
			}
			
			if(arr[i] - min_no > max_diff){
				max_diff = arr[i] - min_no;
			}
		}
		
		
		return max_diff;
	}
	
	
}
