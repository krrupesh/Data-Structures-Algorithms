package vmware;

public class NoOfConsecutive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int a[] = {1,2,3,4,1};
		
		System.out.println(findCount(a,3));
	}

	
	public static int findCount(int a[], int m){
		int count = 0;
		
		for(int i=0;i<a.length;i++){
			int sum=0;
			String str = "";
			
			for (int j = i; j < a.length-i; j++) {
				for (int k = i; k < a.length - (a.length - i-1); k++) {

					if((a[k]%m==0) && (i==k)){
						count++;
						System.out.println(a[k]);
						break;
					}

					if ((a[k + 1] - a[k]) == 1) {
						sum = sum + a[k];

						str = str + a[k] + ",";

						if (sum % m == 0) {
							count++;
							System.out.println(str);
							str = "";
							break;
						}
					}
				}
				sum = 0;
			}
		}
		
		System.out.println("count : "+count);
		
		if(count>0){
			return count;
		}else{
			return -1;
		}
	}
	
}
