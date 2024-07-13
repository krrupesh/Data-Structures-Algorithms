package pattern.matching;

class PatternMatching {

	/**
	 * write as comment what all use cases you are handling by each code


	 */
	static boolean isMatch(String text, String pattern) {
		// your code goes here
		if (text.length() == 0 && pattern.length() == 0) {
			return true;
		}

		int j = 0, i = 0;
		for (; i < text.length() && j < pattern.length(); i++) {
			System.out.println("Step 1, i= " + i + " , j= " + j);

			if (pattern.charAt(j) == '.') {
				if ((j != pattern.length() - 1) && pattern.charAt(j + 1) == '*') {
				} else {
					j++;
				}
				continue;
			} else if (pattern.charAt(j) == '*') {
				if (text.charAt(i) == pattern.charAt(j - 1)) {

				}
			} else if (pattern.charAt(j) == '.' && pattern.charAt(j + 1) == '*') {
				if ((i + 1 < text.length()) && text.charAt(i) == text.charAt(i + 1)) {
					j = j + 1;
					System.out.println("Step 4, i= " + i + " , j= " + j);
					continue;
					// return false;
				}
			} else if (pattern.charAt(j) == '*' && pattern.charAt(j + 1) == '.') {
				while (text.charAt(i) == pattern.charAt(i + 1)) {
					i++;
				}
			}

			// abc abe*.
			System.out.println("Step 2, i= " + i + " , j= " + j);

			if (text.charAt(i) == pattern.charAt(j)) {
				j++;
			} else if ((j + 1 < pattern.length()) && text.charAt(i) != pattern.charAt(j)
					&& pattern.charAt(j + 1) == '*') {
				j = j + 2;
				if (j < pattern.length() && text.charAt(i) == pattern.charAt(j) || pattern.charAt(j) == '.') {
					if (j < pattern.length() - 1) {
						j++;
						System.out.println("Step 3, i= " + i + " , j= " + j);
					}
				}
			} else if ((j + 2 < pattern.length()) && (text.charAt(i) != pattern.charAt(j))
					&& (pattern.charAt(j + 1) == '*' && pattern.charAt(j + 2) == '.')) {
				if (i == text.length() - 1) {
					return true;
				}
			} else {
				return false;
			}

		}// end for

		// a a.*
		if (j < pattern.length() && pattern.charAt(j) == '.' && pattern.charAt(j + 1) == '*') {
			return true;
		}

		if (i != text.length()) {
			return false;
		}

		return true;

	}

	// https://leetcode.com/problems/regular-expression-matching/description/

	public static void main(String[] args) {

		String text = "acd";
		String pattern = "ab*c.";
		
		System.out.println(isMatch(text, pattern));
	}

}