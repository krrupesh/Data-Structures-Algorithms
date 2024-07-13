package hacker.test.oct20;
class Solution
{
public static void main(String[] args) {
int arr1[]={4,6, 9, 20, 56};
int arr2[]={1, 7, 25, 45, 70};
int[] result=mergeLists(arr1, arr2);
for (int j=0; j<result.length;j++)
System.out.print(result[j]+ " ");
}
 
static int[] mergeLists(int[] arr1, int[] arr2)
{
int arr1_Length=arr1.length;
int arr2_Length=arr2.length;
int[] result = new int[arr1_Length + arr2_Length];
int i=0, j = 0;
 
for(int k = 0 ; k< (arr1_Length + arr2_Length);k++)
{
//If arr1 is empty, copy the elements of the array arr2 to the result array .
//When i equals the length of array arr1 , copy the remaining elements in the array arr2 to result
if ( i >= arr1_Length ) //for step1 & 4
{
result[k] = arr2[j];
j ++;
}
//If arr2 is empty, copy the elements of the array arr1 to the result array .
//When j equals the length of array arr2 , copy the remaining elements in the array arr1 to result
else if ( j >= arr2_Length) // For step 2 &amp; 4
{
result[k] = arr1[i];
i ++;
}
else
{
 
if ( arr1[i] < arr2[j]) // step 3
{
result[k] = arr1[i];
i ++;
}
else
{
result[k] = arr2[j];
j ++;
}
}
}
return result;
}
}