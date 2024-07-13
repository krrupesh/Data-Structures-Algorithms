package goldman.sach;

import java.util.Stack;

public class PatternSmallestNumber {

	public static void main(String[] args) {

		System.out.println(PrintMinNumberForPattern("MN"));
	}

	static int PrintMinNumberForPattern(String s) {
		String result = "";
		if (s.isEmpty() || null == s || s.length() > 8)
			return -1;
		if (!validate(s))
			return -1;
		char seq[] = s.toCharArray();
		Stack<Integer> stk = new Stack<Integer>();
		for (int i = 0; i <= seq.length; i++) {
			stk.push(i + 1);
			if (i == seq.length || seq[i] == 'N') {
				while (!stk.empty()) {
					result += stk.peek();
					stk.pop();
				}
			}
		}
		return Integer.parseInt(result);
	}

	static boolean validate(String s) {
		char seq[] = s.toCharArray();
		for (int i = 0; i < seq.length; i++) {
			if (!(seq[i] == 'M') && !(seq[i] == 'N'))
				return false;
		}
		return true;
	}

}
