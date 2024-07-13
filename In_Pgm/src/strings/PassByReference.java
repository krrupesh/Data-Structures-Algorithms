package strings;

public class PassByReference {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		String str = "Rupesh";
		
		System.out.println(str);
		
		modifyString(str);

	}

	private static void modifyString(String str) {

		str = "Modified" + str;
	}

}
