package freshwork;

import java.util.HashMap;
import java.util.Map;

public class DistinctPairs {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	int a[] =  {6,
				12,
				3,
				9,
				3,
				5,
				1
				  };

		System.out.println(numberOfPairs(a, 12));

		long l = 10;

		long k = l - 5;

	}

	// Complete the numberOfPairs function below.
	static int numberOfPairs(int[] a, long k) {

		Map<Long, Long> map = new HashMap<Long, Long>();
		int count = 0;
		int decrement = 0;


		for (int i = 0; i < a.length; i++) {

			long l = a[i];
			Long p = map.put(l, l);
			System.out.println(" p "+p+" l "+l);
			
			if(l == k-l && p != null){
				decrement = 1;
			}
			
		}
		// System.out.println(map);

		for (int i = 0; i < a.length; i++) {
			long l = a[i];

			// System.out.println((map.containsKey(a[i])));

			if (map.containsKey(l) && map.containsKey(k - l)) {
				 System.out.println(l+" "+(k-l));
				if (l != k-l) {
					map.remove(l);
					map.remove(k - l);
					count++;
				}
			}
		}

		
		System.out.println(decrement);
		return count-decrement;
	}

}
