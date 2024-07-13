package amazon;

import java.util.Collections;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//Collections.sort(list);
		
		long a = 100001;
		long b = 100001;
		
		long p = a*b;
		
		System.out.println(p);
		
		int arr [] =  {8, 22, 7, 9, 31, 19, 5, 13};
		
		findSwaps(arr);
		
		
		findSwaps(arr);
	}
	
	
	public static void findSwaps(int arr[]){
		/*long swaps = 0;
		
		for(int i=0;i<arr.length;i++){
			for(int j=i;j<arr.length;j++){
				if(arr[j]<arr[i]){
					swaps++;
				}
			}
			
		}
		
		System.out.println(swaps);
		
		return swaps;*/
		
		
		
		int max = 31;//getMax(arr);
		int[] freq = new int[max + 1];

		for (int idx = 0; idx < arr.length; ++idx)
		{
		    ++freq[arr[idx]];
		}

		int smallerCount = 0;
		for (int idx = 0; idx < freq.length; ++idx) {
		    int temp = freq[idx];
		    freq[idx] = smallerCount;
		    smallerCount = smallerCount + temp;
		}

		for (int idx = 0; idx < arr.length; ++idx) {
		    int ele = arr[idx];
		    System.out.println(freq[ele]);
		}
		
	}
	
	

}
