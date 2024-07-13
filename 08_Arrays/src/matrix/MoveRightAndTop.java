package matrix;

public class MoveRightAndTop {

	public static void main(String[] args) {

		int grid[][] = {  { 0, 0, 0, 0, 7 }, { 0, 0, 0, 0, 6 },{ 0, 0, 0, 0, 5 }, { 0, 1, 1, 1, 5 }, { 2, 0, 0, 0, 0 } };

		//findMaxSum(grid);
		
		String str = "Hellow ";
		
		stringTest(str);
		
		System.out.println(str);
	}

	public static void findMaxSum(int grid[][]) {

		int sum = grid[grid.length - 1][0];
		
		System.out.println(sum);

		for (int i = grid.length - 1;i > 0;) {
			for (int j = 0; j < grid[i].length && i > 0;) {

				if (i > 0 && j < grid[i].length - 1) {

					if (grid[i - 1][j] > grid[i][j + 1]) {
						sum = sum + grid[i - 1][j];
						i--;
					} else {
						sum = sum + grid[i][j + 1];
						j++;
					}
					System.out.println("i " + i + " j, " + j+", sum-0 : "+sum);

				}
				
				if (j == grid[i].length - 1 && i-- > 2) {
					sum = sum + grid[i - 1][j];
					
					System.out.println("i " + i + " j, " + j+", sum-1 : "+sum);
				}
				
				if(i == 0 && j++ < grid[i].length - 2){
					sum = sum + grid[i][j+1];
					System.out.println("i " + i + " j, " + j+", sum-2 : "+sum);
				}

				System.out.println("i " + i + " j, " + j+", sum-3 : "+sum);
			}
		}
		

	}
	
	
	public static void stringTest(String str){
		
		str = str + "World !";
		
		System.out.println("1 "+str);
		
	}

}
