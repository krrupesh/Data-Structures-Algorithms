package goldman.sach1;

import java.util.*;

public class Solution1 {

	public static void main(String[] args) {

		List<List<Integer>> matrix = new ArrayList<List<Integer>>();

		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		List<Integer> l3 = new ArrayList<Integer>();

		l1.add(1);
		l1.add(3);
		l1.add(4);

		l2.add(5);
		l2.add(2);
		l2.add(9);

		l3.add(1);//
		l3.add(7);
		l3.add(6);

		matrix.add(l1);
		matrix.add(l2);
		matrix.add(l3);

		System.out.println(findCount(matrix));
		
		
		Set<Integer> set = new HashSet<Integer>();
		set.add(1);
	}

	public static int findCount(List<List<Integer>> matrix) {

		
		int count = 0;
		List<Boolean> duplicate = new ArrayList<Boolean>();

		for (int i = 0; i < matrix.size(); i++) {			

			for (int j = 0; j < matrix.get(i).size(); j++) {

				if(isMaxORMin(matrix.get(i).get(j),i,j,matrix, duplicate)){
					
					count++;
				}

			}
			
		}

		System.out.println("duplicate "+duplicate);
		
		if(duplicate.size() >0){
			return -1;
		}

		
		return count;
	}
	
	
	public static boolean isMaxORMin(int a, int i, int j,List<List<Integer>> matrix, List<Boolean>  duplicateFound){
		boolean isfound = false;
		
		Set<Integer> setTotal = new HashSet<Integer>();
		Set<Integer> duplicate = new HashSet<Integer>();

		
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		// row fix
		for(int m=0;m<matrix.get(i).size();m++){
			if(matrix.get(i).get(m) < min){
				min = matrix.get(i).get(m);
			}
			
			if(matrix.get(i).get(m) > max){
				max = matrix.get(i).get(m);
			}
			
			if(!setTotal.add(matrix.get(i).get(m))){
				duplicate.add(matrix.get(i).get(m));
			}
		}
		
		if(a == min || a==max){
			isfound = true;
			if(duplicate.contains(a)){
				duplicateFound.add(true);
			}
		}
		
		
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
		
		setTotal.clear();
		duplicate.clear();

		
		// col fix
		for(int m=0;m<matrix.size();m++){
			if(matrix.get(m).get(j) < min){
				min = matrix.get(m).get(j);
			}
			
			if(matrix.get(m).get(j) > max){
				max = matrix.get(m).get(j);
			}
			
			if(!setTotal.add(matrix.get(m).get(j))){
				duplicate.add(matrix.get(m).get(j));
			}
		}
		
		
		if(a == min || a==max){
			isfound = true;
			
			if(duplicate.contains(a)){
				duplicateFound.add(true);
				System.out.println("duplicateFound "+duplicateFound);

			}
		}
	
		return isfound;
	}

}
