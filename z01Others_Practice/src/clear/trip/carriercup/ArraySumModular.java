package clear.trip.carriercup;

public class ArraySumModular {

	public static void main(String[] args) {

		// int A[] = {1,2,3,2,1};

		int A[] = { 1, 1, 2, 2, 3 };

		findTotalOrderedPairs(A, 2, 1, 0);
	}

	static int longestChain(String[] arr) {
		int N = arr.length;
		int[] dp = new int[N];
		dp[0] = 1;

		for (int i = 1; i < N; i++) {
			int max = 1;
			for (int j = i - 1; j >= 0; j--) {
				if (arr[i].count - arr[j].count > 0) {
					if (arr[i].count - arr[j].count == 1) {
						String arrI = arr[i].str;
						for (int rem = 0; rem < arrI.length(); rem++) {
							String afterR = arrI.substring(0, rem)
									+ arrI.substring(rem + 1, arrI.length());
							if (afterR.equals(arr[j].str)) {
								if (dp[j] + 1 > max) {
									max = dp[j] + 1;
								}
							}
						}
					} else {
						break;
					}
				}
			}
			dp[i] = max;
		}

		int max = Integer.MIN_VALUE;
		for (int i = 0; i < N; i++) {
			if (dp[i] > max) {
				max = dp[i];
			}
		}
		System.out.println(max);
	}

	
	
	
	
	
	
	
	public static void findTotalOrderedPairs(int A[], int K, int X, int Y) {

		int count = 0;

		for (int i = 0; i < A.length; i++) {
			for (int j = i; j < A.length; j++) {
				if (i != j) {
					if (((A[i] + A[j]) % K == X) && ((A[i] * A[j]) % K == Y)) {
						count++;

						System.out.println("A[" + i + "]=" + A[i] + " ,A[" + j
								+ "]=" + A[j] + ",  count = " + count);

					}
				}
			}
		}

	}

}
