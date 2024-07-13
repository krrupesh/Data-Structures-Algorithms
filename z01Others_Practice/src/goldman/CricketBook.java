package goldman;
import java.io.*;
 
class CricketBook 
{
	static int count = 0;
	 static int noOfWaysToDrawTheGame(int totalScore) {

	        int size = 100;
	        int[] arr = new int[size];
	        
		 printCompositions(arr,totalScore, 0);
		 
		 //System.out.println(count);
	 return count;	 
	 }
	
    static void printCompositions(int arr[], int n, int i)
    {
    	int [] scores = {2,4,6};
    	if (n == 0)
        {
    		count++;
            printArray(arr, i);
        }
        else if(n > 0)
        {
            for (int k = 0; k < scores.length; k++)
            {
                arr[i]= scores[k];
                printCompositions(arr, n-scores[k], i+1);
            }
        }
    }
     
    static void printArray(int arr[], int m)
    {
        for (int i = 0; i < m; i++)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
     
     
    // Driver program
    public static void main (String[] args) 
    {
        int n = 6;
        int size = 100;
        int[] arr = new int[size];
        System.out.println("Different compositions formed by 1, 2 and 3 of "+ n + " are");
        //printCompositions(arr, n, 0);
        
        noOfWaysToDrawTheGame(6);
    }
}