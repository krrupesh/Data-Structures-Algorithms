package excel.sheet.column;

import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {

	static String str = "";

	
	public static void main(String[] args) {
		
		int coulmn = 26;		
		
		/*for(int i =0;i<=coulmn; i++){
			getCoulmnName(i);
		}*/	
		
	    /*printString(26);
	    printString(51);
	    printString(52);
	    printString(80);
	    printString(676);
	    printString(702);*/
	    printString(52);

	}

	
	static void printString(int n){
		System.out.println();
	    List list = new ArrayList<>();  // To store result (Excel column name)
	 
	    while (n>0){
	        // Find remainder
	        int rem = n % 26;
	 
	        // If remainder is 0, then a 'Z' must be there in output
	        if (rem == 0) {
	        	list.add('Z');
	            n = (n/26)-1;
	        }else{	        
	            list.add((char)(rem-1 + 65));
	            n = n/26;
	        }
	        
	        System.out.println("n : "+n+" rem : "+rem+" list : "+list);
	    }
	 	    
	    getReversedChar(list);
	    
	    return;
	}
	
	public static void getReversedChar(List list){
		
		for(int i = list.size()-1; i>=0;i--){
			System.out.print(list.get(i));
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void getCoulmnName1(int columnno){
	 	int count = 0;
		String str = "";
		
		while(columnno > 25){
			columnno = columnno - 26;
			count++;
		}
		
		int rem = columnno % 26;
		
		System.out.println("count : "+count+" rem : "+rem);
		
		if(count > 0){
			str = str + getChar(count - 1);
		}		
		
		str = str + getChar(rem);
			
		System.out.println(str);
	}
	
	public static char getChar(int no){
		
		return (char)(no + 65);
	}
	
	
	/*
	 * code working for 2 characters
	 
	 	int count = 0;
		String str = "";
		
		while(columnno > 25){
			columnno = columnno - 26;
			count++;
		}
		
		int rem = columnno % 26;
		
		System.out.println("count : "+count+" rem : "+rem);
		
		if(count > 0){
			str = str + getChar(count - 1);
		}		
		
		str = str + getChar(rem);
			
		System.out.println(str);
	 
	 */
}
