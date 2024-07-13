package sorted.matrix;

public class SerachElementInSortedMatrix {

	public static void main(String[] args) {
				
		int mat1[][] = {{10, 20, 30, 40},
		                {15, 25, 35, 45},
		                {27, 29, 37, 48},
		                {32, 33, 39, 50}};


		int mat2[][] = {{10, 20, 30, 40},
		                {15, 25, 35, 45},
		                {27, 29, 37, 48},
		                {32, 33, 39, 50}};
		
		
		serachElementInSortedMatrix(mat1, 50);
		
	}
	
	/**
	 * 
	1. start from right top corner.
	2. if the key is smaller come left else go down.
	3. Complexity O(m+n).
	4.
	 */
	public static void serachElementInSortedMatrix(int mat[][], int key){
		int ROW = mat.length - 1;
		int COL = mat[0].length - 1;
		
		int row = 0;
		int col = COL;
		
		while(row <= ROW && col >= 0){
			
			if(key == mat[row][col]){
				System.out.println("Found at ("+row+","+col+")");
				return;
			}
			
			if(key < mat[row][col]){
				col--;
			}else{
				row++;
			}
		}
		
		System.out.println("Element not found !");
		 
	}
}
