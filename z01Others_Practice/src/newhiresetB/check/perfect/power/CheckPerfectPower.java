package newhiresetB.check.perfect.power;

public class CheckPerfectPower {

	public static void main(String[] args) {

		System.out.println(" Is Perfect Power : "+checkPerfectPower(64,4));		
	}
	
	public static boolean checkPerfectPower(int a, int b){		
		
		int q = a;
		
		while(Math.abs(q)>b){
			q = q/b;
						
			if(q == b){
				return true;
			}
		}
		
		return false;		
	}

}
