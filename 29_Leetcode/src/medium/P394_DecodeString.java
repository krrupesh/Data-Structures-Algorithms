package medium;

import java.util.Stack;

public class P394_DecodeString {

	public static void main(String[] args) {

		String s = "xy[a2[c]]mn";
		// Output: "accaccacc"

		decodeString(s);

	}

	/**
	 * Steps 1.
	 * 
	 * 
	 */
	public static void decodeString(String s) {

		Stack<Character> stack = new Stack<>();

		String str = "";
		for (int i = 0; i < s.length(); i++) {

			//1. if next char is ']' then we need to pop from stack else push
			if (s.charAt(i) == ']') {
				char poped = 0;
				str = "";
				//2. while poped element is not equal to '[' , keep poping from stack and 
				//   concatenate to a string
				while (!stack.isEmpty() && !((poped = stack.pop()) == '[')) {
					System.out.println("If : " + s.charAt(i) + " popped from stack : " + poped + " At i = " + i);
					str = poped + str;
				}

				//3. if peek of stack is digit then pop it and push all previously poped
				//   characters to stack for the count times
				if(Character.isDigit(stack.peek())){
					int count = Integer.parseInt(stack.pop().toString());
					System.out.println("count poped from stack : " + count);
					push(str, stack, count);
				}else{
					//4. if there is no digit before bracket then count = 1
					push(str, stack, 1);
				}
			} else {
				//5. push items to stack
				System.out.println("Else : push to stack : " + s.charAt(i));
				stack.push(s.charAt(i));
			}
		}

		System.out.println(stack.toString().replaceAll(",", "").replace("[", "").replace("]", ""));
	}

	// method to push elements into stack count no of times
	public static void push(String str, Stack<Character> stack, int count) {
		for (int i = 0; i < count; i++) {

			for (int j = 0; j < str.length(); j++) {
				stack.push(str.charAt(j));
				System.out.println("push to stack with count : " + stack);
			}

		}
	}

}
