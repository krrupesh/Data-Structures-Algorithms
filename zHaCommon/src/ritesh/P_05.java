package ritesh;

public class P_05 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int n = 12, res=1;
		
		while(n>5){
			n-=5;
			res*=5;
		}
		
		System.out.println(n*res);
	}

}
