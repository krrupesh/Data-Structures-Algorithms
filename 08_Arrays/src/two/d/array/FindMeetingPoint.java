package two.d.array;

/* Java program to find best
meeting point in 2D array*/
import java.util.*;

class FindMeetingPoint {

	static int ROW = 3;
	static int COL = 5;

	static int minTotalDistance(int grid[][]) {
		if (ROW == 0 || COL == 0)
			return 0;

		Vector<Integer> vertical = new Vector<Integer>();
		Vector<Integer> horizontal = new Vector<Integer>();

		// Find all members home's position
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (grid[i][j] == 1) {
					vertical.add(i);
					horizontal.add(j);
				}
			}
		}

		// Sort positions so we can find most
		// beneficial point
		Collections.sort(vertical);
		Collections.sort(horizontal);

		// middle position will always beneficial
		// for all group members but it will be
		// sorted which we have alredy done
		int size = vertical.size() / 2;
		int x = vertical.get(size);
		int y = horizontal.get(size);

		// Now find total distance from best meeting
		// point (x,y) using Manhattan Distance formula
		int distance = 0;
		for (int i = 0; i < ROW; i++)
			for (int j = 0; j < COL; j++)
				if (grid[i][j] == 1)
					distance += Math.abs(x - i) + Math.abs(y - j);

		return distance;
	}

	// Driver code
	public static void main(String[] args) {
		int grid[][] = { { 1, 0, 1, 0, 1 }, { 0, 1, 0, 0, 0 }, { 0, 1, 1, 0, 0 } };
		System.out.println(minTotalDistance(grid));
	}
}

// This code is contributed by 29AjayKumar
