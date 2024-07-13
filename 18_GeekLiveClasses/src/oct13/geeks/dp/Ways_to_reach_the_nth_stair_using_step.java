package oct13.geeks.dp;

public class Ways_to_reach_the_nth_stair_using_step {

	public static void main(String[] args) {
		
		int ways = noofwaysRec(4);
		System.out.println(ways);
		
	}
	
	
	public static int noofwaysDP(int n){
		
		int res[] = new int[n];
		res[0] = 1;
		res[1] = 1;
		res[2] = 2;
		
		for(int i = 3; i<n;i++){
			res[i] = res[i-1] + res[i-2] + res[i-3];
		}
		
		return -1;
	}
	
	
	
	public static int noofwaysRec(int n){
		
		if(n == 1 || n == 0){
			return 1;
		}
		
		if(n == 2){
			return 2;
		}		
		
		return  noofwaysRec(n-1) + 
				noofwaysRec(n-2) + 
				noofwaysRec(n-3);
		
	}

}
