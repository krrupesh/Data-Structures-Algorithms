package hacker.test.oct20;
class SecondLargestNumber{
 
public static void main(String args[])
{ 
 
	  int numbers[] = {1,1,1,1};
	  findSecondMax(numbers);

 
 }
 
  public static void findSecondMax(int numbers[]){
	  int highest = Integer.MIN_VALUE;
	  int second_highest  = Integer.MIN_VALUE;
	   
	  for(int n:numbers){
	   
		  if(highest < n){
		   
		        second_highest = highest;
		        highest =n;
		   
		   } /*else if(second_highest < n){
		   
		      second_highest = n;
		   
		  }*/
	   
	  }
	  System.out.println(second_highest);
  }

}