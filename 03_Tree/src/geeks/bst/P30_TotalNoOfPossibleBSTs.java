package geeks.bst;

public class P30_TotalNoOfPossibleBSTs {


	public static void main(String[] args) {

		findTotalNoOfBSTs(9);
	}

	
	public static void findTotalNoOfBSTs1(int n){		
		
		int arr[] = new int[n+1];
		arr[0] = 1;
		arr[1] = 1;
		
		int sum = 0;
	
		for(int i=2;i<=n;i++){
			
			for(int j=1;j<=i;j++){
				sum = sum + arr[j-1]*arr[i-j];
			}
			
			arr[i] = sum;
			sum = 0;
		}
		
		System.out.println("sum : "+arr[n]);
	}
	
	
	public static void findTotalNoOfBSTs(int n){		
		
		int arr[] = new int[n+1];
		arr[0] = 1;
		arr[1] = 1;
		
		int sum = 0;
		
		System.out.println("########## arr[0]="+arr[0]);
		System.out.println("########## arr[1]="+arr[1]);

		
		for(int i=2;i<=n;i++){
			
			for(int j=1;j<=i;j++){
				System.out.println("arr["+(j-1)+"]="+arr[j-1]);
				System.out.println("arr["+(i-j)+"]="+arr[i-j]);
				System.out.println("-------------------");

				sum = sum + arr[j-1]*arr[i-j];

			}
			
			arr[i] = sum;
			sum = 0;
			System.out.println("########## arr["+i+"]="+arr[i]);
		}
		
		System.out.println("sum : "+arr[n]);
	}
	
	
}	