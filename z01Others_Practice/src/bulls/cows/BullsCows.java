package bulls.cows;

public class BullsCows {

	public static void main(String[] args) {

		String secret = "1807"; 
		String guess = "7810";
		
		String hint = getHint(secret, guess);
		
		System.out.println("hint : "+hint);
		
	}
	
	
	public static String getHint(String secret, String guess){
		
		String result = "";
		
		char secretArr[] = secret.toCharArray();
		char guessArr[] = guess.toCharArray();

		int matched = 0;
		int unmatched = 0;
		
		for(int i = 0;i<secretArr.length;i++){
			if(secretArr[i] == guessArr[i]){
				matched++;
			}else{
				unmatched++;
			}
		}
		
		return ""+matched+"A"+unmatched+"B";
	}

}
