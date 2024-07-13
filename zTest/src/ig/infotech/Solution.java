package ig.infotech;

import java.util.regex.Pattern;

public class Solution {

	public static void main(String[] args) {

	/*	String s = "Forget   CVs..Save time . x x";

		int maxcount = findmaxCount(s);
		System.out.println(maxcount);*/
		

	}

	public static int findmaxCount(String S) {

		Pattern ptn = Pattern.compile("\\.|\\?|\\!");
		String[] parts = ptn.split(S);
		int maxSize = 0;
		for (String p : parts) {
			String words[] = p.trim().split("\\s+");
	
			int size = words.length;
						
			if(maxSize<size){
				maxSize = size;
			}
		}

		return maxSize;
	}

}
