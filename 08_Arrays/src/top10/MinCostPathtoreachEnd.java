package top10;

public class MinCostPathtoreachEnd {

	
	public static void main(String[] args) {

		int arr[][] = {{1,2,3},{4,8,2},{1,5,3}};
		
		findMinCostPath(arr);
		
	}

	
	public static void findMinCostPath(int cost[][]){
		
		int minCost[][] = new int[cost.length][cost[0].length];
		
		int row = minCost.length;
		int col = minCost[0].length;
		
		minCost[0][0] = cost[0][0];
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				

				
				if(i !=0 || j!=0){
					
					if(i>0 && j>0){					
						minCost[i][j] = min(minCost[i-1][j-1],minCost[i-1][j],minCost[i][j-1]);
					}else if(i == 0 ){
						minCost[i][j] = minCost[i][j-1];
					}else if(j==0){
						minCost[i][j] = minCost[i-1][j];
					}
					
					minCost[i][j] = minCost[i][j] + cost[i][j];
				}		
				
				System.out.println("Min cost : i ="+i+" j = "+j+" , "+minCost[i][j]);

			}
		}
		
		System.out.println("Min cost : "+minCost[row-1][col-1]);
		
	}


	private static int min(int a, int b, int c) {
		
		int d = a < b ? a : b;
		
		return (c < d ? c : d);
	}
}
