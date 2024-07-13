package shuffle.string.check;

import java.util.HashMap;
import java.util.Scanner;

public class ShuffleStringCheck {

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			String str = null;
			do {
				System.out.println("Enter the string for shuffle check:");
				str = scanner.nextLine();
			} while (str == null || str.trim().isEmpty());
			checkIfStringIsSufflable(str);
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
	}

	public static void checkIfStringIsSufflable(String str) {

		HashMap<Character, Integer> map = new HashMap<>();
		int count = 0, maxCount = 0, sum = 0;

		for (int i = 0; i < str.length(); i++) {
			if (map.containsKey(str.charAt(i))) {
				count = map.get(str.charAt(i));
				map.put(str.charAt(i), ++count);
			} else {
				count = 1;
				map.put(str.charAt(i), count);
			}

			if (maxCount < count) {
				maxCount = count;
			}
		}

		System.out.println(map);
		System.out.println("maxCount : " + maxCount);

		sum = str.length() - maxCount;
		System.out.println("sum of other letters: " + sum);

		if (maxCount <= sum + 1) {
			System.out.println("YES !! it is shufflable");
		} else {
			System.out.println("It can't be shuffled");
		}
	}

}
