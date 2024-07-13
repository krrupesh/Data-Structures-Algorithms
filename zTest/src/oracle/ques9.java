package oracle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ques9 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while(!(s=r.readLine()).startsWith("4")){
			System.out.println(s);
		}
	}

}
