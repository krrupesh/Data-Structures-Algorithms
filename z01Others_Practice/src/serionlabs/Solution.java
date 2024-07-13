package serionlabs;

public class Solution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	/*	int i1 =10;
		int i2 = 10;
		inc(i1);
		inc(i2);
		
		System.out.println(i1);
		System.out.println(i2);
		
		new Solution().doHandle();*/
		
		String s1 = null;
		String s2 = "abcd";
		
		String s3 = s1 + s2;
		System.out.println(s3);
		
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void inc(int input){
		input++;
	}
	
	public void doHandle(){
		try{
			method();
		}catch(OutOfMemoryError error){
			System.out.println("Out of memory boccured");
			System.gc();
			System.out.println("hiiiii");
		}
	}
	
	public void method(){
		throw new OutOfMemoryError("---");
	}
}
