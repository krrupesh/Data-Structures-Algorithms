package palindroms;

public class CheckIfStringHavingDigitsIsPalindrome {

	public static void main(String[] args) {

		String str = "m a 343 la y a l amx";

		int i, j;

		boolean isPalindrome = true;

		for (i = 0, j = str.length() - 1; i < j; i++, j--) {

			while (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
				i++;
			}

			while (!(str.charAt(j) >= 'a' && str.charAt(j) <= 'z')) {
				j--;
			}

			if (str.charAt(i) != str.charAt(j)) {
				isPalindrome = false;
			}

		}

		if (isPalindrome) {
			System.out.println("isPalindrome : " + isPalindrome);
		} else {
			System.out.println("isPalindrome : " + isPalindrome);
		}

		// continue test
		for (int k = 0; k < 11; k++) {
			System.out.println("k : " + k);

			if (k < 5) {
				continue; // it wont go down, it will go to for loop
			}

			System.out.println("k : " + k + k);

		}

	}

}
