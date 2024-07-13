package easy;

public class MinimumDeletionAlternatingCharacters {

	public static void main(String[] args) {

		String str = "AAABAAAB";
		System.out.println("before "+str);
	
		minimumDeletionAlternatingCharacters(str);
	}
	
	public static void minimumDeletionAlternatingCharacters(String str){
		
		StringBuffer sb = new StringBuffer(str);
		
		for(int i = 0;i<sb.length()-1;i++){
			
			if(sb.charAt(i) == sb.charAt(i+1)){
				sb.deleteCharAt(i+1);
				i--;
			}
			
		}
		
		System.out.println(sb.toString());
		
	}

}
