import java.io.*;
import java.util.*;

class Solution {

	static int indexEqualsValueSearch(int[] arr) {
		// your code goes here
		// 0 1 2 3 4 5
		// -5, 0, 2,3,10,29]
		return binarySearchIterative(arr, 0, arr.length - 1, 500);

	}

	static int binarySearch(int[] arr, int start, int end, int lowestIndex) {
		if (start > end) {
			return -1;
		}

		int mid = (start + end) / 2;

		System.out.println("Step 1 arr[mid] = " + arr[mid] + ", mid = " + mid);

		if (arr[mid] == mid) {
			if (lowestIndex > mid) {
				lowestIndex = mid;
			}
			return lowestIndex;
		} else if (arr[mid] > mid) {
			System.out.println("Step 2 arr[mid] = " + arr[mid] + ", mid = " + mid);

			return binarySearch(arr, start, mid - 1, lowestIndex);
		} else {
			System.out.println("Step 3 arr[mid] = " + arr[mid] + ", mid = " + mid);

			return binarySearch(arr, mid + 1, end, lowestIndex);
		}

	}

	
	static int binarySearchIterative(int[] arr, int start, int end, int x) {
		
		int lowestIndex = 500;
		
		while(start <= end){
			int mid = (start + end)/2;		
			if(arr[mid] == mid){
				if(lowestIndex > mid){
					lowestIndex = mid;
				}		
				end = mid-1;
			}else if(arr[mid] > mid){
				end = mid-1;
			}else{
				start = mid + 1;
			}
		}
			
		return lowestIndex;
	}
	
	
	
	public static void main(String[] args) {
		int[] arr = { -5, 0, 2, 3, 10, 29, 40 };
		// int []arr = {-8,0,2,5};
		int x = indexEqualsValueSearch(arr);
		System.out.println(x);
	}

}