package triplet.sum;

import java.util.Arrays;

public class TripletSum {

	public static void main(String[] args) {

		int[] nums = {2,2,2,2,5,6,7,8};
		//int[] nums = {1,2,3,4,5,6,7,8};

		int sum = 6;
		
		findTriplets(nums,sum);
			
	}
	
	
	// will just count can be found in lesser complexity
	public static void findTriplets(int a[], int sum){
		
		Arrays.sort(a);
		
		for(int i=0;i<a.length;i++){
			
			int l = i + 1;
			int r = a.length - 1;
			
			while(l < r){
				
				if(a[i]+a[l]+a[r]==sum){
					System.out.println(a[i]+","+a[l]+","+a[r]+" -> "+i+l+r);
					break;
					//r--;
				}else if(a[i]+a[l]+a[r]<sum){
					l++;
				}else{
					r--;
				}
				
			}
			
		}
		
	}

}
