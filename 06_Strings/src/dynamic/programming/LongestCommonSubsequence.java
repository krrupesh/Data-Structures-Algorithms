package dynamic.programming;

public class LongestCommonSubsequence {

	public static void main(String[] args) {

		/*String str1 = "dacbcfzy";
		String str2 = "abcdacfy";*/
		
		String str1 = "fcrxzwscanmligyxyvym";
		String str2 = "jxwtrhvujlmrpdoqbisbwhmgpmeoke";

		char ch1[] = str1.toCharArray();
		char ch2[] = str2.toCharArray();

		findLongestCommonSubSequence(ch1, ch2);

	}

	public static void findLongestCommonSubSequence(char[] ch1, char[] ch2) {

		System.out.println("ch1 "+ch1.length+" ch2 "+ch2.length);
		
		int maxCount = 0;
		int Im = 0, Jm = 0;
		StringBuffer sb = new StringBuffer();

		int l1 = ch1.length + 1;
		int l2 = ch2.length + 1;
		int M[][] = new int[l1][l2];
		
		for (int i = 1; i <= ch1.length; i++) {
			for (int j = 1; j <= ch2.length; j++) {

				if (ch1[i - 1] == ch2[j - 1]) {
					M[i][j] = M[i - 1][j - 1] + 1;
				} else {
					M[i][j] = Math.max(M[i - 1][j], M[i][j - 1]);
				}

				if (M[i][j] > maxCount) {
					maxCount = M[i][j];
					Im = i;
					Jm = j;
				}
			}
		}

		System.out.println("maxCount : " + maxCount);

		// finding the common sub-sequence
		while (Im > 0 && maxCount != 0) {

			while (Jm > 0 && maxCount != 0) {

				if (ch1[Im - 1] == ch2[Jm - 1]) {
					sb.append(ch1[Im - 1]);
					Im--;
					Jm--;
					maxCount--;
				} else if (M[Im][Jm] == M[Im][Jm - 1]) {
					Jm--;
				} else if (M[Im][Jm] == M[Im - 1][Jm]) {
					Im--;
				}
			}

		}

		System.out.println(sb.reverse());
	}

}
