package test;

public class Hello {

	static int x=7;
	
	
	public static void main(String[] args) {
		String s = "";
		
		for(int y=0;y<3;y++){
			x++;
			switch(x){
				case 7: s +="7 ";
				case 8: s +="8 ";
				case 9: s +="9 ";
				case 10: {s +="10 "; break;}
				default: s+="d ";
				case 13: s +="13 ";
			}
		}
		
		System.out.println(s);
	}
	{x++; }
}
