package top.bottom.left.right.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class PrintFourViews {

	public static void main(String[] args) {

		PrintFourViews obj = new PrintFourViews();

		BinarySearchTree bst = new BinarySearchTree();
		/*
		 * bst.insert(90); bst.insert(100); bst.insert(10);
		 * 
		 * obj.printTopView(bst.root);
		 */

		/*bst.insert(25);
		bst.insert(65);
		bst.insert(10);
		obj.printBottomView(bst.root);*/
		
		bst.insert(25);
		bst.insert(24);
		bst.insert(23);
		bst.insert(21);
		bst.insert(10);
		//obj.printLeftView(bst.root);
		obj.printRightView(bst.root);

	}

	
	public void printTopView(Node root) {

		LinkedList list = new LinkedList();
		printTopView(root, list, 0);
	}

	public void printTopView(Node root, 
			LinkedList list, int hd) {

		if (root == null) {
			return;
		}

		if (!list.contains(hd)) {
			list.add(hd);
			System.out.print(" ," + root.key);
		}

		printTopView(root.left, list, hd - 1);

		printTopView(root.right, list, hd + 1);
	}

	// print bottom view
	public void printBottomView(Node root) {

		HashMap<Integer, Integer> map = new HashMap();
		map = printBottomView(root, map, 0);

		for (Map.Entry<Integer, Integer> e : map.entrySet()) {
			System.out.print(e.getValue() + " ,");
		}
	}

	public HashMap<Integer, Integer> printBottomView(Node root, 
			HashMap<Integer, Integer> map, int hd) {

		if (root == null) {
			return null;
		}

		map.put(hd, root.key);

		printBottomView(root.left, map, hd - 1);

		// bring left here if you want left to be printed
		printBottomView(root.right, map, hd + 1);

		return map;
	}

	// print the left view of tree
	public void printLeftView(Node root) {

		Queue<Node> q = new LinkedList<Node>();

		q.offer(root);
		q.offer(null);

		System.out.print(q.peek().key + " ,");
		
		while (!q.isEmpty()) {

			Node n = q.poll();

			if (n != null) {

				if (n.left != null) {
					q.offer(n.left);
				}

				if (n.right != null) {
					q.offer(n.right);
				}
			} else {
				if (!q.isEmpty()) {
					q.offer(null);
				
				// print the leftmost element
				// when level changes
				 System.out.print(q.peek().key);
				}
			}
		}
	}	
	

	// print the right view of tree
	public void printRightView(Node root) {

		Queue<Node> q = new LinkedList<Node>();

		q.offer(root);
		q.offer(null);

		System.out.print(q.peek().key + " ,");
		
		while (!q.isEmpty()) {

			Node n = q.poll();

			if (n != null) {

				if (n.right != null) {
					q.offer(n.right);
				}
				
				if (n.left != null) {
					q.offer(n.left);
				}
			} else {
				if (!q.isEmpty()) {
					q.offer(null);
					
					// print the leftmost element
					// when level changes
					System.out.print(q.peek().key);
				}
			}
		}

	}
	
	
}
