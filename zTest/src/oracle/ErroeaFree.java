package oracle;

public class ErroeaFree {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}
	
	public void fly(int test){
		final int e = 1;
		class FlyingQEquation{
			{
				System.out.println(e);
				System.out.println(test);
			}
		}
	}

}
