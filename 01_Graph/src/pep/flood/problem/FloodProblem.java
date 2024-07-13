package pep.flood.problem;

import java.util.Scanner;

public class FloodProblem {

	/**
	 * input 
	 * 
4
3
0
1
1

0
0
1

1
0
0

0
1
0

	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
		int row = scanner.nextInt();
		int column = scanner.nextInt();
		
		int mat[][] = new int[row][column];
		boolean visited[][] = new boolean[row][column];
		
		for(int i=0;i<mat.length;i++){
			for(int j=0;j<mat[0].length;j++){
				mat[i][j] = scanner.nextInt();
				//System.out.print(mat[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("Input reading completed");

				
		findPath(mat, visited, 0, 0, "");
		
	}
	
	
	public static void findPath(int mat[][], boolean visited[][], int row, int col, String psf){
		
		// 1. set boundary conditions 
		if(row < 0 || col < 0 || row == mat.length || col == mat[0].length 
				|| mat[row][col] == 1 || visited[row][col]){
			return;
		}
		
		// 2. print path at boundary condition
		if(row == mat.length -1 && col == mat[0].length-1){
			System.out.println(psf);
			return;
		}
		
		// 3. mark node/cell visited 
		visited[row][col] = true;		
		
		// 4. traverse all 4 sides of a cell [t,l,d,r]
		findPath(mat, visited,  row - 1,  col, psf +"t");
		findPath(mat, visited,  row ,  col - 1, psf +"l");
		findPath(mat, visited,  row + 1,  col, psf +"d");
		findPath(mat, visited,  row ,  col + 1, psf +"r");
		
		// 5. unvisit the path if it doesn't reach to destination
		visited[row][col] = false;
				
	}
	
	

}
