package substring.repeatation.k.length;

public class SubStringRepeatation {

	public static void main(String[] args) {

		String str ="abcbedabcabc";
		
		String st[] = str.split("abc");
		
		System.out.println(st.length);
		
		for(int i=0;i<st.length;i++){
			System.out.println("string : "+st[i]);
		}
		str = str.replaceAll(st[1],"abc");
		
		System.out.println("str : " +str);
	}

}
