package newhiresetB.delimeter.matching;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DelimeterMatching {

	public static void main(String[] args) {


		String str = "This is a (good(boy) and ( that )is ( not";
		int size = getUnMatchedDelimeterCount(str);
		
		System.out.println("size "+size);
	}
	
	
	public static int getUnMatchedDelimeterCount(String str){
		
		if((str == null) || (str.length() == 0)){
			return -1;
		}
		
		char chArray[] = str.toCharArray();
		Stack<Character> stack = new Stack<Character>();
		
		for(int i=0;i<chArray.length;i++){
			
			if((chArray[i]=='(') || (chArray[i]=='{') || (chArray[i]=='[')){
				stack.push(chArray[i]);
				
			}else if((chArray[i]==')')){
				if((!stack.isEmpty()) && (stack.peek() == '(')){
					stack.pop();
				}
			}else if((!stack.isEmpty()) && (chArray[i]=='}')){
				if(stack.peek() == '{'){
					stack.pop();
				}
			}else if((!stack.isEmpty()) && (chArray[i]==']')){
				if(stack.peek() == '['){
					stack.pop();
				}
			}
		}
		
		return stack.size();
		
	}

}
