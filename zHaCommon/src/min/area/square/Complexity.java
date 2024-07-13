package min.area.square;

public class Complexity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(Calculate(10));
	}

	static int i = 0;
	static double 	Calculate(double a){
		System.out.println(i++);
		if(a<=2){
			return 1;
		}else
			return (Calculate(Math.floor(Math.sqrt(a))) + a);
	}
}
