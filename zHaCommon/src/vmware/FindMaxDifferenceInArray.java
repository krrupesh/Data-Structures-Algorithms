package vmware;

public class FindMaxDifferenceInArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		int a[] = {7,2,3,10,2,4,8,1};
		
		System.out.println(findMaxDiff(a));
	}
	
	public static int findMaxDiff(int a[]){
		
		int max_diff = Integer.MIN_VALUE;
		
		int count = 0;
		for(int i=0;i<a.length-1;i++){
			if(a[i]>a[i+1]){
				count++;
			}
		}
		
		if(count == a.length-1){
			return -1;
		}
		
		for(int i=1;i<a.length;i++){
			for(int j=0;j<i;j++){
				if (a[i]>a[j]) {
					if ((a[i] - a[j]) > max_diff) {
						max_diff = a[i] - a[j];
					}
				}
			}
		}
		
		if(max_diff == Integer.MIN_VALUE){
			return -1;
		}else{
			return max_diff;
		}
		
	}

}
