package ritesh;

public class P_06 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int a[] = {-5,-7,8,-1,-2};
		int z =a[0],n=5,i=0,c=a[0];
		
		for(i=1;i<n;i++){
			c=max(a[i],c+a[i]);
			z=max(z,c);
		}
		
		System.out.println(z);
	}
	
	
	static int max(int x,int y){
		return(y>x)?y:x;
	} 

}
