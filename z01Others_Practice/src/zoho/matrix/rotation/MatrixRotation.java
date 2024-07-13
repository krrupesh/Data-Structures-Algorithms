package zoho.matrix.rotation;

public class MatrixRotation {

	public static void main(String[] args) {

		int a[][] = {{1,2,3},{4,5,6},{7,8,9},{-1,-2,-3}};
		printMatrix(a);
		
		int [][]a90 = rotateBy90(a);
		printMatrix(a90);
		
		int [][]a180 = rotateBy180(a);
		printMatrix(a180);
		
		int [][]a270 = rotateBy270(a);
		printMatrix(a270);

	}

	
	public static int[][] rotateBy90(int [][]a){
		System.out.println("Rotated by 90 degree");
		int row = a.length;
		int col = a[0].length;
		System.out.println("row "+row+" col "+col);
		int a90[][]=new int[row][col];
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				a90[i][j]=a[col-1-j][i];
			}
		}
				
		return a90;
	}
	
	public static int[][] rotateBy180(int [][]a){
		System.out.println("Rotated by 180 degree");
		int row = a.length;
		int col = a[0].length;
		int a180[][]=new int[row][col];
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				a180[i][j]=a[row-1-i][col-1-j];
			}
		}
				
		return a180;
	}
	
	public static int[][] rotateBy270(int [][]a){
		System.out.println("Rotated by 270 degree");
		int row = a.length;
		int col = a[0].length;
		int a270[][]=new int[row][col];
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				a270[i][j]=a[j][row-1-i];
			}
		}
				
		return a270;
	}
	
	public static void printMatrix(int a[][]){
		int row = a.length;
		int col = a[0].length;
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				System.out.print(a[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
	}
}
