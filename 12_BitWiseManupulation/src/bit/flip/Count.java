package bit.flip;

// Count number of bits to be flipped 
// to convert A into B 
import java.util.*;

class Count {

	// Driver code
	public static void main(String[] args) {
		int a = 10;
		int b = 20;
		//System.out.print(FlippedCount(a, b));
		
		System.out.println(testBits(10));
	}
	
	// Function that count set bits
	public static int countSetBits(int n) {
		int count = 0;
		while (n != 0) {
			count += n & 1; // 
			n >>= 1;
		}
		return count;
	}

	// Function that return count of
	// flipped number
	public static int FlippedCount(int a, int b) {
		// Return count of set bits in
		// a XOR b
		return countSetBits(a ^ b);
	}
	
	
	// Function that count set bits
	public static int testBits(int n) {
		int count = 0;
		while (n != 0) {
//			count += n & 1;
//			n >>= 1;
//			System.out.println("count->"+count+", n->"+n);
			
			int m = n & 1;
			
			System.out.println("m->"+m+",n=>"+n);
			n = n >> 1;
		}
		return count;
	}
}