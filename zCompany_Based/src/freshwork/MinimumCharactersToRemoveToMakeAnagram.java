package freshwork;

public class MinimumCharactersToRemoveToMakeAnagram {

	public static void main(String[] args) {

		
		String str1 = "abcdabe";
		String str2 = "abdpqcbeafba";
			
		minCharsToRemoved(str1,str2);
	}
	
	public static void minCharsToRemoved(String str1, String str2){
		
		int arr[] = new int[26];
		
		int l1 = str1.length();
		int l2 = str2.length();
		int l = l1>l2?l1:l2;
		int minCount = 0;
		
		for(int i =0; i<l;i++){
			
			if(i<l1){
				arr[str1.charAt(i)-'a'] = arr[str1.charAt(i)-'a'] + 1;	
			}
			
			if(i<l2){
				arr[str2.charAt(i)-'a'] = arr[str2.charAt(i)-'a'] - 1;
			}						
		}
		
		for(int i=0;i<26;i++){
			if(arr[i] < 0){
				minCount = minCount + -(arr[i]);
				System.out.println("Remove : "+(char)(i+(int)'a'));
			}else if(arr[i] > 0){
				minCount = minCount + arr[i];
				System.out.println("Remove : "+(char)(i+(int)'a'));
			}
		}
		
		System.out.println("minCount : "+minCount);			
	}

}
