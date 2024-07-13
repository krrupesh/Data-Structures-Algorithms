package substring.finding;
import java.io.*;
import java.util.*;

class Solution {

	static int decodeVariations(String S) {
		// your code goes here
    int count = 1;
    for(int i=0;i<S.length()-1;i++){
      if(Integer.parseInt(""+S.chatAt(i)+S.charAt(i+1))- 65 <=26 ){
        count++;
        i++;
      }
    }
    
    
    
	}

	public static void main(String[] args) {
	  
	}
}
