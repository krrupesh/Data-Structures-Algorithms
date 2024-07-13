package knapsack.problem;

public class SubsetSumProblem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int [] arr = {4,2,7,1,3};
		
		
		checkIfElementsMakeToGivenSum(arr, 4);
		checkIfElementsMakeToGivenSum(arr, 7);
		checkIfElementsMakeToGivenSum(arr, 3);
		checkIfElementsMakeToGivenSum(arr, 6);
		checkIfElementsMakeToGivenSum(arr, 8);
		checkIfElementsMakeToGivenSum(arr, 100);
				
		
		//checkIfElementsMakeToGivenSum(arr, -4);
		//checkIfElementsMakeToGivenSum(arr, 21);
		//checkIfElementsMakeToGivenSum(arr, 0);
	}
	
	public static void checkIfElementsMakeToGivenSum(int arr[], int sum){
		
		// step 1
		boolean [][]mat = new boolean[arr.length+1][sum+1];
		
		// step 2
		for(int i = 0;i<arr.length+1;i++){
			mat[i][0] = true;
		}
				
		for(int i=1;i<mat.length;i++){
			for(int j=1;j<mat[0].length;j++){
				// Step 3, taking value from top if its true
				if(mat[i-1][j]){
					mat[i][j] = mat[i-1][j];
				}else if(arr[i-1] <= j){
					// Step 4, going back by j-arr[i-1] position
					mat[i][j] = mat[i-1][j - arr[i-1]];
				}
			}
		}
	}
	
	public static void printMatrix(boolean [][] mat){
		
		for(int i=0;i<mat.length;i++){
			for(int j=0;j<mat[0].length;j++){
				System.out.print(mat[i][j]+" ");				
			}
			System.out.println();
		}
		
	}

}
