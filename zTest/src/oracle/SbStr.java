package oracle;

public class SbStr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	StringBuffer sb = new StringBuffer("abc");
		sb.append("def");
		
		String str = new String("milky");
		str.concat(" way");
		String s= str+" way galaxy"; //
*/		
		
		String stmt = "This is here to hel";
		for(String token : stmt.split("//s")){
			System.out.println(token +" ");
		}
		
	}

}
