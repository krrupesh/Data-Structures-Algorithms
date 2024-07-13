package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Chain implements Comparable<Chain>{
	String str;
	int count;
	
	Chain(String str){
		this.str = new String(str);
		count = str.length();
	}
	
	public int compareTo(Chain p){
		return this.count - p.count;
	}
}
public class StringChain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner inp = new Scanner(System.in);
		int N = inp.nextInt();
		inp.nextLine();

		inp.close();
	}
	
	public int find(String[] words){
		int N = words.length;
		Chain[]arr = new Chain[N];
		
		
		for(int i=0; i<N; i++){
			arr[i] = new Chain(words[i]);
		}
		Arrays.sort(arr);
		return compute(arr);
	}
	
	int  compute(Chain[] arr){
		int N = arr.length;
		int[]dp = new int[N];
		dp[0] = 1;
		
		for(int i=1; i<N; i++){
			int max = 1;
			for(int j=i-1; j>=0; j--){
				if(arr[i].count - arr[j].count > 0){
					if(arr[i].count - arr[j].count == 1){
						String arrI = arr[i].str;
						for(int rem = 0; rem<arrI.length(); rem++){
							String afterR = arrI.substring(0, rem) + arrI.substring(rem+1, arrI.length());
							if(afterR.equals(arr[j].str)){
								if(dp[j] + 1 > max){
									max = dp[j]+1;
								}
							}
						}
					}
					else{
						break;
					}
				}
			}
			dp[i] = max;
		}
		
		int max = Integer.MIN_VALUE;
		for(int i=0; i<N; i++){
			if(dp[i] > max){
				max = dp[i];
			}
		}
		System.out.println(max);
		
		return max;
	}

}