package walmart;

public class NearestPerfectSquare {

	public static void main(String[] args) {

		int num = 12;// 9,16
		
		int res = (int)(Math.sqrt((float)num) + .5f);

		System.out.println(res*res);
		
	}

}
