package kmp.algorithm;

public class KMP {

	public static void main(String[] args) {

		String str = "abcxabcdabcdabcy";
		String subString = "abcdabcy";
		KMP kmp = new KMP();

		boolean result = kmp.KMPAlgorithm(str.toCharArray(), subString.toCharArray());
		
		System.out.print(result);

	}

	private int[] computePatternArray(char[] charArr) {
		int[] patternTempArr = new int[charArr.length];

		int j = 0;
		for (int i = 1; i < charArr.length;) {

			if (charArr[j] == charArr[i]) {
				patternTempArr[i] = j + 1;

				i++;
				j++;
			} else {
				if (j != 0) {
					j = patternTempArr[j - 1];
				} else {
					patternTempArr[i] = 0;
					i++;
				}
			}
		}

		return patternTempArr;
	}

	private boolean KMPAlgorithm(char[] text, char[] pattern) {

		int tempArr[] = computePatternArray(pattern);
		printArray(tempArr);
		
		int j = 0;
		for(int i=0; i < text.length && j < pattern.length;){
			
			if(text[i] == pattern[j]){
				i++;
				j++;
			}else{
				if(j != 0){					
					j = tempArr[j-1];
				}else{					
					i++;
				}
				
			}
			
		}
		
		if(j == pattern.length){
			return true;
		}

		return false;
	}

	public void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + ",");
		}
	}
}
