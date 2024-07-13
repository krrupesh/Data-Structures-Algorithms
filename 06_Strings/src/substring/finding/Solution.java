package substring.finding;

import java.util.*;

class Solution {

	static String getShortestUniqueSubstring(char[] arr, String str) {
		int headIndex = 0;
		String result = "";
		int uniqueCounter = 0;
		Map<Character, Integer> countMap = new HashMap<Character, Integer>();

		for (int i = 0; i < arr.length; i++) {
			countMap.put(arr[i], 0);
		}
		for (int tailIndex = 0; tailIndex < str.length(); tailIndex++) {
			char tailChar = str.charAt(tailIndex);

			if (!countMap.containsKey(tailChar)) {
				continue;
			}

			int tailCount = countMap.get(tailChar);
			if (tailCount == 0) {
				uniqueCounter++;
			}

			countMap.put(tailChar, tailCount + 1);

			System.out.println("before while, uniqueCounter ="+uniqueCounter +" countMap = "+countMap);
			while (uniqueCounter == arr.length) {
				System.out.println("1. tailIndex = "+tailIndex +" headIndex = "+headIndex+" uniqueCounter "+uniqueCounter+" countMap "+countMap+" result = "+result);
				int tempLength = tailIndex - headIndex + 1;
				if (tempLength == arr.length) {
					System.out.println("ans point tempLength = "+tempLength);
					return str.substring(headIndex, tailIndex + 1);
				}

				if (result == "" || tempLength < result.length()) {
					result = str.substring(headIndex, tailIndex + 1);
				}

				char headChar = str.charAt(headIndex);
				
				// why we are doing this ?
				/**
				 * if we enter this while loop it means that sub string is present 
				 *    in between headIndex and tailIndex. 
				 * now between these two indexes we need to find the smallest sub string 
				 *    for this we will have to increment headIndex
				 * if we increment headIndex we may loose any unique element inside our range
				 * so to include that unique element we come out of this while loop and once
				 *   that unique element comes into our range then we again enter into while loop
				 *        
				 */
				if (countMap.containsKey(headChar)) {
					int headCount = countMap.get(headChar) - 1;
					if (headCount == 0) {
						uniqueCounter--;
					}
					countMap.put(headChar, headCount);
				}

				headIndex++;
				System.out.println("2. tailIndex = "+tailIndex +" headIndex = "+headIndex+" uniqueCounter "+uniqueCounter+" countMap "+countMap+" result = "+result);

			}
		}
		return result;
	}

	public static void main(String[] args) {
		char[] arr = { 'x', 'y', 'z' };
		String str = "xyyzyzyx";

		// Expected output: "zyx"
		System.out.println(getShortestUniqueSubstring(arr, str));
	}
}