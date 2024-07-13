package array.rotation;

public class FindClosetElementInSortedArray {

	public static void main(String[] args) {

		int arr[] = {3,5,6,10,15,20,22,26};
		
		findClosestElement(arr,17,0,arr.length-1);
		
	}
	
	// find Closet Element in Sorted Array
	public static void findClosestElement(int a[], int k,int start, int end){
		
		System.out.println("start->"+start+" ,end->"+end);
		
		if(start == end){
			return;
		}
		
		int mid = (start + end)/2;
		
		if(k >= a[mid] && k <= a[mid+1]){
			int d1 = k - a[mid];
			int d2 = a[mid + 1] - k;
			
			if(d1 > d2){
				System.out.println(a[mid + 1]);
			}else{
				System.out.println(a[mid]);
			}
			
			return;
		}else if(k >= a[mid-1] && k <= a[mid]){
			int d1 = k - a[mid-1];
			int d2 = a[mid] - k;
			
			if(d1 > d2){
				System.out.println(a[mid]);
			}else{
				System.out.println(a[mid-1]);
			}
			return;
		}else if(k < a[mid]){
			findClosestElement(a,k,start,mid-1);
		}else{
			findClosestElement(a,k,mid+1,end);
		}		
	}

}
