package string.compress;

public class StringTest {

	public static void main(String[] args) {

		String str = "aaabbbbbcc";
		
		//str = str.substring(0, 3);
		
		System.out.println("str : "+str);
		
		str = str.substring(0,1)+3+str.substring(3, 4)+5+str.substring(8, 9)+2;
		
		System.out.println("str : "+str);

		
	}

}
