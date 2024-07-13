package intu;

import java.util.Arrays;

public class FindMinimumStops {

	public static void main(String[] args) {

		
		int arr[][] = {{1,3,7},{3,6,4},{1,1,5},{7,2,4}};
		
		System.out.println(arr.length);
		
		arr = sort2DArray(arr);
		

		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[i].length;j++){
				System.out.print("arr["+i+"]["+j+"] = "+arr[i][j]+" ");
			}
			System.out.println();
		}
		
		int n = findStops(arr);
		
		System.out.println("\nMin no of stops are : "+n);
	}
	
	
	public static int findStops(int[][] arr){
		int c[] = new int[arr[0].length];
		int minsum = 0;
		arr = sort2DArray(arr); 
		
		for(int i=0;i<arr[0].length;i++){
			int max = Integer.MIN_VALUE;
			for(int j=0;j<arr.length;j++){
				if(arr[j][i]>max){
					max = arr[j][i];
				}
			}
			c[i] = max;
		}
		
		System.out.println("min no are ");
		for(int i = 0;i<c.length;i++){
			minsum = minsum +c[i];
			System.out.print(c[i]+" ");
		}
		
		return minsum;
	}
	
	
	public static int[][] sort2DArray(int[][] arr){
		
		for(int i=0;i<arr.length;i++){
			arr[i] = sort1DArray(arr[i]);
		}	
				
		return arr;
	}
	
	public static int[] sort1DArray(int[] arr){
				
		  int n = arr.length;
          int temp = 0;
         
          for(int i=0; i < n; i++){
                  for(int j=1; j < (n-i); j++){
                         
                          if(arr[j-1] > arr[j]){
                                  //swap the elements!
                                  temp = arr[j-1];
                                  arr[j-1] = arr[j];
                                  arr[j] = temp;
                          }
                         
                  }
          }
		
		return arr;
	}

}
