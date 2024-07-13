package zoho.character.occurence;

// xxxyycddbbxx ans: x3y2cd2b2x2
public class CharacterOccurence {

	public static void main(String[] args) {

		String str = "xxxyycddbbxx";
		
		System.out.println(getCharOccurence(str));
	}
	
	
	public static String getCharOccurence(String str){
		String newStr = "";
		
		char [] charArray = str.toCharArray();
		int count = 1;
		int i = 0;
		for(i=0;i<charArray.length-1;i++){
			
			System.out.println("charArray["+i+"]="+charArray[i]);
			if(charArray[i] == charArray[i+1]){
				count++;
			}else{
				if(count>1){
					newStr = newStr+charArray[i]+count;
				}else{
					newStr = newStr+charArray[i];
				}
				count = 1;
			}
		}
		
	
		if(count>1){
			newStr = newStr+charArray[i]+count;
		}else{
			newStr = newStr+charArray[i];
		}		
		
		return newStr;
	}
	

}
