package max.sum.increasing.subsequence;

public class MaxSumIncreasingSubSequence {

	public static void main(String[] args) {

		int a[] ={4,6,1,3,8,4,6};
		
		findMaxSumIncreasingSubSequence(a);
	}
	
	
	public static void findMaxSumIncreasingSubSequence(int a[]){
		
		int Amax[] = new int[a.length];
		int Aindex[] = new int[a.length];
		int Imax = 0;
		int maxSum  = 0;
		int noOfElementsInSubsequence = 0;
		
		for(int i = 0;i<a.length;i++){
			Amax[i] = a[i];
			Aindex[i] = i;
		}
		
		for(int i = 0;i<a.length;i++){
			for(int j=0;j<i;j++){
				
				if(j<i){
					if (a[i]>a[j]) {// since we are finding the increasing subsequence
						if (Amax[j] + a[i] > Amax[i]) {
							Amax[i] = Amax[j] + a[i];
							Aindex[i] = j;
							
							if(Amax[i]>maxSum){
								maxSum = Amax[i];
								Imax = i;
								
								noOfElementsInSubsequence++;
							}
							
						} else {
							j++;
						}
					}else{
						j++;
					}
				}
			}
			System.out.println("Amax["+i+"]="+Amax[i]);

		}
		
		System.out.println("---------------------------------------");
		
        for(int k = 0;k<noOfElementsInSubsequence;k++){
        	System.out.println(a[Imax]+" Imax = "+Imax);
        	Imax = Aindex[Imax];
        }
		
	}

}
