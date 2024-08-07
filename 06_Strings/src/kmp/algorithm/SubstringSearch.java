package kmp.algorithm;

public class SubstringSearch {

	public static void main(String args[]) {

		String str = "abcxabcdabcdabcy";
		String subString = "abcdabcy";
		SubstringSearch ss = new SubstringSearch();
		boolean result = ss.KMP(str.toCharArray(), subString.toCharArray());
		System.out.print(result);

	}
	
	/**
	 * Slow method of pattern matching
	 */
	public boolean hasSubstring(char[] text, char[] pattern) {
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < text.length && j < pattern.length) {
			if (text[i] == pattern[j]) {
				i++;
				j++;
			} else {
				j = 0;
				k++;
				i = k;
			}
		}
		if (j == pattern.length) {
			return true;
		}
		return false;
	}

	/**
	 * Compute temporary array to maintain size of suffix which is same as
	 * prefix Time/space complexity is O(size of pattern)
	 */
	private int[] computeTemporaryArray(char pattern[]) {
		int[] lps = new int[pattern.length];
		int index = 0;
		for (int i = 1; i < pattern.length;) {
			if (pattern[i] == pattern[index]) {
				lps[i] = index + 1;
				index++;
				i++;
			} else {
				if (index != 0) {
					index = lps[index - 1];
				} else {
					lps[i] = 0;
					i++;
				}
			}
		}
		return lps;
	}

	/**
	 * KMP algorithm of pattern matching.
	 */
	public boolean KMP(char[] text, char[] pattern) {

		int lps[] = computeTemporaryArray(pattern);
		printArray(lps);
		
		int i = 0;
		int j = 0;
		while (i < text.length && j < pattern.length) {
			if (text[i] == pattern[j]) {
				i++;
				j++;
			} else {
				if (j != 0) {
					j = lps[j - 1];
				} else {
					i++;
				}
			}
		}
		if (j == pattern.length) {
			return true;
		}
		return false;
	}

	public void printArray(int []arr){
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+",");
		}
	}
}