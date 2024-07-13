package newhiresetB.sum.digits.singledigit;

public class SumDigitsSingleDigit {

	public static void main(String[] args) {


		System.out.println("sum : "+findSumOfDigitsAsSingleDigit(55));
	}
	
	public static int findSumOfDigitsAsSingleDigit(int a){
		
		int sum = 0;
		
		while(a!=0){
			sum = sum + a%10;
			a = a/10;
		}
		
		if(!(sum<=9)){
			return findSumOfDigitsAsSingleDigit(sum);
		}else{
			return sum;
		}
				
	}

}
