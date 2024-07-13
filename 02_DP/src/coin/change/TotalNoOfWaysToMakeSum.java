package coin.change;

/**
 * @Date 22/05/2016
 * @author Rupesh kumar
 *
 *         Given a total(sum of coins) and coins of certain denominations find
 *         total number of ways can be formed from coins assuming infinity
 *         supply of coins
 *         
 *         there is another problem in which all permutations are required.
 *
 */
public class TotalNoOfWaysToMakeSum {

	public static void main(String[] args) {

		 //int coin[] = {1,2};
		int coin[] = { 1, 3, 4 };

		findMinNoOfCoins(coin, 6);
	}

	public static void findMinNoOfCoins(int[] coin, int vol) {

		int cost[][] = new int[coin.length + 1][vol + 1];

		for (int i = 1; i <= coin.length; i++) {
			cost[i][0] = 1;
		}

		printMatrix(cost);

		for (int i = 1; i <= coin.length; i++) {
			for (int j = 1; j <= vol; j++) {

				if (j < coin[i - 1]) {
					cost[i][j] = cost[i - 1][j];
				} else {
					cost[i][j] = cost[i - 1][j] + cost[i][j - coin[i - 1]];
				}

				printMatrix(cost);
			}
			System.out.println();
		}

		System.out.println(cost[coin.length][vol]);
	}

	public static void printMatrix(int arr[][]) {
		System.out.println("printing matrix\n--------------------");
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}
