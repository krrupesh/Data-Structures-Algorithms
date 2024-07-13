package goldman.sach1;

import java.util.*;

public class Solution2 {

	public static void main(String[] args) {

		List<List<Integer>> matrix = new ArrayList<List<Integer>>();

		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		List<Integer> l3 = new ArrayList<Integer>();

		l1.add(1);
		l1.add(0);
		l1.add(0);
		l1.add(1);

		l2.add(0);
		l2.add(1);
		l2.add(1);
		l2.add(1);

		l3.add(1);
		l3.add(0);
		l3.add(0);
		l3.add(1);

		matrix.add(l1);
		matrix.add(l2);
		matrix.add(l3);

		System.out.println(findCount(matrix));
	}

	public static int findCount(List<List<Integer>> matrix) {

		int count = 0;

		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {

				if (i > 0 && j + 1 < matrix.get(i).size()) {
					if ((matrix.get(i).get(j) == 1) && (matrix.get(i - 1).get(j + 1) == 1)) {
						count++;
					}
				}

				if (j + 1 < matrix.get(i).size()) {
					if ((matrix.get(i).get(j) == 1) && (matrix.get(i).get(j + 1) == 1)) {
						count++;
					}
				}

				if (j + 1 < matrix.get(i).size() && i + 1 < matrix.size()) {
					if ((matrix.get(i).get(j) == 1) && (matrix.get(i + 1).get(j + 1) == 1)) {
						count++;
					}
				}

				if (i + 1 < matrix.size()) {
					if ((matrix.get(i).get(j) == 1) && (matrix.get(i + 1).get(j) == 1)) {
						count++;
					}
				}

			}
		}

		return count;
	}

}
