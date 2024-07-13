package easy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidString {

	public static void main(String[] args) {

		// String str =
		// "ibfdgaeadiaefgbhbdghhhbgdfgeiccbiehhfcggchgghadhdhagfbahhddgghbdehidbibaeaagaeeigffcebfbaieggabcfbiiedcabfihchdfabifahcbhagccbdfifhghcadfiadeeaheeddddiecaicbgigccageicehfdhdgafaddhffadigfhhcaedcedecafeacbdacgfgfeeibgaiffdehigebhhehiaahfidibccdcdagifgaihacihadecgifihbebffebdfbchbgigeccahgihbcbcaggebaaafgfedbfgagfediddghdgbgehhhifhgcedechahidcbchebheihaadbbbiaiccededchdagfhccfdefigfibifabeiaccghcegfbcghaefifbachebaacbhbfgfddeceababbacgffbagidebeadfihaefefegbghgddbbgddeehgfbhafbccidebgehifafgbghafacgfdccgifdcbbbidfifhdaibgigebigaedeaaiadegfefbhacgddhchgcbgcaeaieiegiffchbgbebgbehbbfcebciiagacaiechdigbgbghefcahgbhfibhedaeeiffebdiabcifgccdefabccdghehfibfiifdaicfedagahhdcbhbicdgibgcedieihcichadgchgbdcdagaihebbabhibcihicadgadfcihdheefbhffiageddhgahaidfdhhdbgciiaciegchiiebfbcbhaeagccfhbfhaddagnfieihghfbaggiffbbfbecgaiiidccdceadbbdfgigibgcgchafccdchgifdeieicbaididhfcfdedbhaadedfageigfdehgcdaecaebebebfcieaecfagfdieaefdiedbcadchabhebgehiidfcgahcdhcdhgchhiiheffiifeegcfdgbdeffhgeghdfhbfbifgidcafbfcd";
		String str = "xxxaabbccrry";

		//System.out.println(checkValidString(str));
		
		
		//checkStringValidity();
	}

	public static String checkValidString(String str) {

		HashMap<Character, Integer> map = new HashMap<Character, Integer>();

		for (int i = 0; i < str.length(); i++) {

			if (!map.containsKey(str.charAt(i))) {
				map.put(str.charAt(i), 1);
			} else {
				map.put(str.charAt(i), map.get(str.charAt(i)) + 1);
			}
		}

		System.out.println(map); // 1122

		int arr[] = new int[map.size()];
		int i = 0;
		for (Map.Entry<Character, Integer> e : map.entrySet()) {
			arr[i++] = e.getValue();
		}

		Arrays.sort(arr);

		int diff = 0;
		for (int j = 0, k = arr.length - 1; (j < 2 && k > -1); j++, k--) {
			diff = diff + arr[k] - arr[j];

			System.out.println("j " + j);

			if (diff > 1 && arr.length >= 2) {
				if ((arr[0] == 1) && arr[1] != 1) {
					return "YES";
				}
			} else if (diff > 1) {
				System.err.println("diff " + diff);
				return "NO";
			}
		}

		return "YES";
	}

	public static void checkStringValidity(String s) {

		//String s = "xxxaabbccrry";
		HashMap<Character, Integer> map = new HashMap(26);
		for (Character c : s.toCharArray()) {
			if (map.containsKey(c)) {
				int val = map.get(c);
				map.put(c, ++val);
			} else {
				map.put(c, 1);
			}
		}
		int min = Collections.min(map.values());
		int max = Collections.max(map.values());
		HashMap<Integer, Integer> intMap = new HashMap();
		for (int count : map.values()) {
			Integer val = intMap.get(count);
			if (null != val) {
				intMap.put(count, ++val);
			} else {
				intMap.put(count, 1);
			}
		}

		String output = intMap.size() == 1 || (intMap.size() <= 2 && (intMap.get(min) <= 1 || intMap.get(max) <= 1))
				? "YES" : "NO";
		System.out.println(output);
	}

}
