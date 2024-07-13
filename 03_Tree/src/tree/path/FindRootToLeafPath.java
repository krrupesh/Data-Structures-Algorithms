package tree.path;

import java.util.LinkedList;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class FindRootToLeafPath {

	public static void main(String[] args) {

		BinarySearchTree bst = new BinarySearchTree();
		bst.inorder();

		int[] path = new int[1000];
		printPathsRecur(bst.root,path,0);
		
		//LinkedList<Integer> list = new LinkedList<>();
		//findRootToLeafPath(bst.root, list);

	}


	/*
	 * Recursive helper function -- given a node, and an array containing the
	 * path from the root node up to but not including this node, print out all
	 * the root-leaf paths.
	 */
	public static void printPathsRecur(Node node, int path[], int pathLen) {
		
		if (node == null)
			return;

		/* append this node to the path array */
		path[pathLen] = node.key;
		pathLen++;
		
		/* it's a leaf, so print the path that led to here */
		if (node.left == null && node.right == null)
			printArray(path, pathLen);
		else {
			/* otherwise try both subtrees */
			printPathsRecur(node.left, path, pathLen);
			printPathsRecur(node.right, path, pathLen);
		}
	}

	/* Utility function that prints out an array on a line. */
	public static void printArray(int ints[], int len) {
		int i;
		for (i = 0; i < len; i++) {
			System.out.print(ints[i] + " ");
		}
		System.out.println("");
	}
	

	public static void findRootToLeafPath(Node root, LinkedList<Integer> list) {

		System.out.println(list + " , " + list.hashCode() + " ->Outside If");

		if (root.left == null && root.right == null) {
			list.add(root.key);
			System.out.println(list + " , " + list.hashCode());
			list = new LinkedList<>();

			return;
		} else {
			list.add(root.key);
		}

		findRootToLeafPath(root.left, list);
		findRootToLeafPath(root.right, list);

	}

}
