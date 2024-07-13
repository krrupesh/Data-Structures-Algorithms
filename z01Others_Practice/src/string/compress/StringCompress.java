package string.compress;

public class StringCompress {

	public static void main(String[] args) throws Exception {

		String str = "ababc";

		compressString(str);
	}
	
	public static void compressString(String str) throws Exception{
		
		int strLength = str.length();
		boolean throwException = true;
		
		int s = 1;
		int count = 1;
		
		int l = 0;
		int initializei = 0;
		
		for(int i = 0;i<str.length()-1;i++){
			
			//System.out.println("str.charAt("+i+")="+str.charAt(i)+" : str.charAt("+(i+1)+")="+str.charAt(i+1));
			if(str.charAt(i) == str.charAt(i+1)){
				
				count++;
				
				if(count>1){
					throwException = false;
				}
			}else{
				
				str = str.substring(0, s)+ count + str.substring(s+count-1, str.length());
				System.out.println("count : "+count+" str : " +str);

				String scount = ""+count;
				l = scount.length();
				
				initializei = initializei+1+l;
				
				s = initializei+1;
				i = initializei-1;
				count = 1; // resetting count for each character
				
				System.out.println("i : "+i);
			}
		}
		
		str = str.substring(0, s)+ count + str.substring(s+count-1, str.length());
		System.out.println("Outside count : "+count+" str : " +str);
		
		if(throwException){
			throw new Exception("String is already compressed");
		}
		
	}
}
