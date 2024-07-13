package array.rotation;

public class FindRotationCount {

	public static void main(String[] args) {

		int a[] = {6,10,15,20,22,25,3,5};
		
		//int a[] = {3,5,6,10,15,20,22,25};
		
		//int a[] = {25,3,5,6,10,15,20,22};
		
		//int a[] = {3, 4, 5, 1, 2};
		
		System.out.println(findRotationCount(a,0,a.length-1));
	}
	
	static int count = 0;
	public static int findRotationCount(int a[] ,int start ,int end){
		//count++;
		
		System.out.println("start->"+start+" ,end->"+end);

		if(start == 0  && end == 0){
			return a[0];
		}/*else if(start == end){
			System.out.println("else start->"+start+" ,end->"+end);

			return -1;
		}*/
		
		int mid = (start + end)/2;
		
		//System.out.println(count+" -> "+a[mid]);
		
		if(a[mid-1] > a[mid]){
			return a[mid];
		}else if(a[mid] < a[end]){
			return findRotationCount(a,start,mid-1);
		}else if(a[mid] > a[end]){
			return findRotationCount(a,mid+1,end);
		}
		
		//System.out.println(count+" -> ");

		return a[0];
	}
	

}
