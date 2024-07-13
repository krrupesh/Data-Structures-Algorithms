package leetcode.medium;
class FindDuplicate {
    // Function to print duplicates
    void printRepeating(int arr[], int size)
    {
        int i;
        System.out.println("The repeating elements are : ");
         
        for (i = 0; i < size; i++) {
            System.out.println("arr-I["+i+"] : "+arr[i]);
            int j = Math.abs(arr[i]);
            if (arr[j] >= 0){
                arr[j] = -arr[j];
                System.out.println("arr-J["+j+"] : "+arr[j]);
            }
            else
                System.out.println(j + " ");
        }
    }
 
    // Driver code
    public static void main(String[] args)
    {
        FindDuplicate duplicate = new FindDuplicate();
        int arr[] = { 1, 6, 3, 1, 3, 6, 2 };
        int arr_size = arr.length;
 
        duplicate.printRepeating(arr, arr_size);
    }
}