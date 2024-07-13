package coin.change;

/**
 * @Date 22/05/2016
 * @author Rupesh kumar
 *
 *         Given a total(sum of coins) and coins of certain denominations find
 *         number of ways total can be formed from coins assuming infinity
 *         supply of coins
 *
 */
public class CoinChangeProblem {

	public static void main(String[] args) {

		int coin[] = { 1, 3, 5 };

		findNoOfWays(coin, 8);
	}

	public static void findNoOfWays(int[] coin, int vol) {

		int cost[][] = new int[coin.length + 1][vol + 1];

		for (int i = 1; i <= coin.length; i++) {
			cost[i][0] = 1;
		}

		for (int i = 1; i <= coin.length; i++) {
			for (int j = 1; j <= vol; j++) {
				if (j < coin[i - 1]) {
					cost[i][j] = cost[i - 1][j];
				} else {
					cost[i][j] = cost[i - 1][j] + cost[i][j - coin[i - 1]];
				}
			}
		}

		System.out.println(cost[coin.length][vol]);// 4

		/*
		 * 
		 * System.out.println("i "+i+" j "+j);
		 * System.out.println("cost[i][j] "+cost[i][j]);
		 * System.out.println("cost[i-1][j] "+cost[i-1][j]);
		 * System.out.println("cost[i][j-coin[i-1]] "+cost[i][j-coin[i-1]]);
		 * 
		 * System.out.println(
		 * "----------------------------------------------------");
		 * 
		 * System.out.println("else i "+i+" j "+j);
		 * System.out.println("else cost[i][j] "+cost[i][j]);
		 * 
		 */

	}
}
