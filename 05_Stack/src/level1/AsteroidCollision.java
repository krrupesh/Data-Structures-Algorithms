package level1;

import java.util.Stack;

public class AsteroidCollision {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//int arr[] = { 40, 20, 10, -30, 20, 30 };
		int arr[] = { 8,-8};

		int[] result = asteroidCollisionWithoutStack(arr);
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + " ,");
		}
	}

	public static int[] asteroidCollisionWithoutStack(int[] asteroids) {
		int n = asteroids.length;
        int j = 0;

        for (int i = 0; i < n; i++) {
            int asteroid = asteroids[i];
            // 
            while (j>0 && asteroids[j-1]>0 && asteroid<0 && asteroids[j-1] < Math.abs(asteroid)) 
            {j--;}

            if (j==0 || asteroid>0 || asteroids[j-1]<0) 
                asteroids[j++] = asteroid;
            else if(asteroids[j-1] == Math.abs(asteroid)) // special case, equal & spposite
                j--;
        }
        
        int[] result = new int[j];
        System.arraycopy(asteroids, 0, result, 0, j);

        return result;
	}
	
	
	
	

	// it gets timeout on leetcode
	public static int[] asteroidCollision(int[] asteroids) {

		// you cannt modify a collection whilte iterating
		Stack<Integer> stack = new Stack();

		int count = 0;
		for (int i = 0; i < asteroids.length; i++) {

			if (i == 0) {
				stack.push(asteroids[i]);
				count++;
			} else if (asteroids[i] > 0) {
				stack.push(asteroids[i]);
				count++;
			} else {
				int peek = stack.peek();

				if (peek > 0) {
					while (peek < (Math.abs(asteroids[i])) && (asteroids[i] < 0)) {
						if (!stack.isEmpty()) {
							stack.pop();
							count--;
						}

					}
				} else {
					stack.push(asteroids[i]);
					count++;
				}

			}
		}
		int ans[] = new int[count];
		while (!stack.isEmpty()) {
			ans[--count] = stack.pop();
		}
		return ans;
	}
}
