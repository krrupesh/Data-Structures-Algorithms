package number.to.words;

public class NumbertoWords {

	public static void main(String[] args) {

		NumbertoWords obj = new NumbertoWords();
		
		//String str = obj.getNumberToString(0150);
		
		//System.out.println("Number In String = "+str);
		
		int a = 0150;
		System.out.println("a : "+a);
		String str = "Rupesh" + 0150;
		System.out.println("str "+str);
		
		
/*		String str = "1125545";
		int l = str.length();
		System.out.println(str.substring(l-5, l-3));
		System.out.println(Integer.parseInt(str.substring(l-5, l-3)));*/
	}

	
	public String getSingleDigitString(int n){
		String str = "";
		switch(n){
		
			case 0 :
				break;
			case 1 : str = "one";
				break;
			case 2 : str = "two";
				break;
			case 3 : str = "three";
				break;
			case 4 : str = "four";
				break;
			case 5 : str = "five";
				break;
			case 6 : str = "six";
				break;
			case 7 : str = "seven";
				break;
			case 8 : str = "eight";
				break;
			case 9 : str = "nine";
		}
	
		return str;
	}
	
	
	public String getDoubleDigitString(int n){
		System.out.println("getDoubleDigitString n "+n);
		String str = "";
		
		if(n<10){
			str = getSingleDigitString(n);
		}
		
		switch(n){
		
			case 10 : str = "ten";
				break;
			case 11 : str = "elleven";
				break;
			case 12 : str = "twelve";
				break;
			case 13 : str = "thirteen";
				break;
			case 14 : str = "fourteen";
				break;
			case 15 : str = "fifteen";
				break;
			case 16 : str = "sixteen";
				break;
			case 17 : str = "seventeen";
				break;
			case 18 : str = "eightteen";
				break;
			case 19 : str = "nineteen";
				break;
		}
	
		if(n>=20 && n<30){
			str = "twenty";
		}else if(n>=30 && n<40){
			str = "thirty";
		}else if(n>=40 && n<50){
			str = "fourty";
		}else if(n>=50 && n<60){
			str = "fifty";
		}else if(n>=60 && n<70){
			str = "sixty";
		}else if(n>=70 && n<80){
			str = "seventy";
		}else if(n>=80 && n<90){
			str = "eighty";
		}else if(n>=90 && n<100){
			str = "ninety";
		}
		
		int rem = n%10;

		if(!(n>=10 && n<20)){
			str = str + getSingleDigitString(rem);
		}
		
		return str;
	}
	
	public String getNumberToString(int n){
		String str = ""+n;
		System.out.println(str);
		String strno = "";
		int l = str.length();
		
		if(l>0){
			if(l>1){
				System.out.println(Integer.parseInt(str.substring(l-2, l))+" str "+str);
				strno = getDoubleDigitString(Integer.parseInt(str.substring(l-2, l)));
			}else{
				strno = getSingleDigitString(str.charAt(l-1));
			}
			System.out.println("strno1 = "+strno);
		}		
		
		if(l>2){
			String st = getSingleDigitString(Integer.parseInt(""+str.charAt(l-3)));
			if(st.length() > 1){
				strno = st + " hundred " + strno;				
			}
			System.out.println("strno2 = "+strno);

		}
		
		if(l>3){
			if(l>4){
				String st = getDoubleDigitString(Integer.parseInt(str.substring(l-5, l-3)));
				if(st.length() > 1){					
					strno = st + " thousand " + strno;
				}
			}else{
				String st = getSingleDigitString(Integer.parseInt(""+str.charAt(l-4)));
				if(st.length() > 1){				
					strno = st + " thousand " + strno;
				}
			}
			System.out.println("strno3 = "+strno);

		}
		
		if(l>5){
			if(l>6){
				String st = getDoubleDigitString(Integer.parseInt(str.substring(l-7, l-5)));
				if(st.length() > 1){					
					strno = st + " lakh " + strno;
				}
			}else{
				String st = getSingleDigitString(Integer.parseInt(""+str.charAt(l-6)));
				if(st.length() > 1){					
					strno = st + " lakh " + strno;
				}
			}
			System.out.println("strno4 = "+strno);

		}
		
	return strno;	
	}
}
