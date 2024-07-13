package ciron;

public class MaxProfit {

	public static void main(String[] args) {

		int P[] = { 1, 2, 3, 4, 9, 8, 16, 27, 32 };

		printMaxProfit(P);
	}

	public static void printMaxProfit(int P[]) {

		int l = P.length;
		int M[][] = new int[l][l];
		int maxProfit = 0;

		M[0][0] = P[0];

		for (int j = 0; j < l; j++) {
			for (int i = 0; i < l; i++) {

				if (i < j) {
					if ((P[j] % P[i] == 0)) {
						M[i][j] = P[j] + M[i][i];

						if (maxProfit < M[i][j]) {
							maxProfit = M[i][j];
						}

						System.out.println("M[" + (i) + "][" + (j) + "]=" + M[i][j] + " ");
					} else if (i > 0) {
						M[i][j] = M[i - 1][j];
					}
				} else if (i == j && i > 0) {
					M[i][j] = M[i - 1][j];
				} /*
					 * else if (i > 0 && j > 0) { M[i][j] = Math.max(M[i -
					 * 1][j], M[i][j - 1]); }
					 */
			}
		}

		System.out.println(maxProfit);

		for (int i = 0; i < P.length; i++) {
			if (i == 0) {
				System.out.print("   ");
			}

			if (P[i] < 10) {
				System.out.print(" " + P[i] + " ");
			} else {
				System.out.print(P[i] + " ");
			}
		}

		System.out.println("\n------------------------------");

		printMatrix(M, P);

	}

	public static void printMatrix(int M[][], int P[]) {
		for (int i = 0; i < M.length; i++) {
			if (P[i] < 10) {
				System.out.print(" ");
			}
			System.out.print(P[i] + "|");
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] < 10) {
					System.out.print(" ");
				}
				System.out.print(M[i][j] + " ");
			}
			System.out.println();
		}
	}

	/*
	 * 
	 * public static void printMaxProfit(int P[]) {
	 * 
	 * int l = P.length; int M[][] = new int[l][l]; int maxProfit = 0;
	 * 
	 * M[0][0] = P[0];
	 * 
	 * for (int j = 0; j < l; j++) { for (int i = 0; i < l; i++) { if (i < j &&
	 * (P[j] % P[i] == 0)) { M[i][j] = P[j] + M[i][i];
	 * 
	 * if (maxProfit < M[i][j]) { maxProfit = M[i][j]; }
	 * 
	 * System.out.println("M[" + (i) + "][" + (j) + "]=" + M[i][j] + " "); }
	 * else if (i == j && i > 0) { M[i][j] = M[i - 1][j]; } else if (i > 0 && j
	 * > 0) { M[i][j] = Math.max(M[i - 1][j], M[i][j - 1]); } } }
	 * 
	 * System.out.println(maxProfit);
	 * 
	 * }
	 * 
	 * 
	 */
}
