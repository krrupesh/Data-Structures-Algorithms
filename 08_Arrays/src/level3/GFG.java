package level3;
// Java program for the above approach
class GFG{

// Function to count triplets
static int CountTriplets(int a[], int n)
{

	// To store count of total triplets
	int ans = 0;

	for (int i = 0; i < n; i++)
	{

		// Initialize count to zero
		int cnt = 0;

		for (int j = i + 1; j < n; j++)
		{

			// If a[j] > a[i] then,
			// increment cnt
			if (a[j] > a[i])
				cnt++;

			// If a[j] < a[i], then
			// it mean we have found a[k]
			// such that a[k] < a[i] < a[j]
			else
				ans += cnt;
		}
	}

	// Return the final count
	return ans;
}

// Driver code
public static void main(String[] args)
{
	int arr[] = { 2, 5, 1, 3, 0 };

	int n = arr.length;

	System.out.print(CountTriplets(arr, n));
}
}

// This code is contributed by shivanisinghss2110
