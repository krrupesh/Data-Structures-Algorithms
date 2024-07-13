package level1;

import java.util.Arrays;
import java.util.List;

public class FindNetWorth {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		int x = 46;
		int y = 20;
		int [] arr = {7,15,11,17};
		
		
		Arrays.sort(arr);
		
		int dp[][][] = new int[arr.length][y][x];
		
		//findNetProfit(x, y, arr);
		//int ans = solve(arr.length-1,0,0,arr,x,y);
		
		int ans = solveDP(arr.length-1,0,0,arr,x,y,dp);

		ans = ans *(1<<y);
		System.out.println(ans);
		
	}
	

	
	public static int solve(int idx, int cnt, int cost, int[] A, int X, int Y) {
	    if(cost > X) return (int) -1e9;
	    if(cost == X || cnt == Y+1 || idx == -1) return 0;
	    	    
	    // pick A[idx]
	    int res1 = A[idx] + solve(idx - 1, cnt + 1, cost + A[idx] * (1 << cnt), A, X, Y) ;
	    	    
	    // don't pick A[idx]
	    int res2 = solve(idx - 1, cnt, cost, A, X, Y);
	    
	    //return dp[idx][cnt][cost] = Math.max(res1, res2);
	    return Math.max(res1, res2);
	}
	
	// Dynamic programming
	public static int solveDP(int idx, int cnt, int cost, int[] A, int X, int Y, int [][][]dp) {
		
	    if(cost > X) return (int) -1e9;
	    if(cost == X || cnt == Y+1 || idx == -1) return 0;
	    
	    // way to convert knapsack recrsion to dp is identify the changing varaibles and put in dp array 
	    //if(dp[idx][cnt][cost] != -1) return dp[idx][cnt][cost];
	    
	    // pick A[idx]
	    int res1 = A[idx] + solveDP(idx - 1, cnt + 1, cost + A[idx] * (1 << cnt), A, X, Y, dp) ;
	    	    
	    // don't pick A[idx]
	    int res2 = solveDP(idx - 1, cnt, cost, A, X, Y, dp);
	    
	    return dp[idx][cnt][cost] = Math.max(res1, res2);
	    //return Math.max(res1, res2);
	}
		
	
	
	public static void findNetProfit(int x, int y, int []arr){
		
		int n = arr.length;
		int dp[][] = new int[n+1][n+1];
		
		for(int i=0; i<n;i++){
			for(int j=0; j<x;j++){
				dp[i][j] = Math.max(dp[i][j-1], dp[i-1][(x-2^i)-1] + 2^i);
			}
		}	
		
		System.out.println(dp[n][n]);
		
	}

}
