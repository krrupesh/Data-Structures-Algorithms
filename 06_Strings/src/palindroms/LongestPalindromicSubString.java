package palindroms;

public class LongestPalindromicSubString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String str = "forgeeksskeegfor";
		findLongestPalindrome(str);

	}

	// brute force approach
	public static void findLongestPalindrome(String str){
		
		char [] arr = str.toCharArray();
		
		for(int i=0;i<arr.length;i++){
			StringBuilder sb = new StringBuilder();
			for(int j=i;j<arr.length;j++){
				sb.append(arr[j]);
				System.out.println(sb.toString());
			}
		}		
		
	}
	
	// Optimized approach
	public static void findLongestPalindromeDP(String str){
		
	}
	
}
