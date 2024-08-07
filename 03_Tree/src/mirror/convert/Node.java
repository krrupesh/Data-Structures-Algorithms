package mirror.convert;

import java.util.LinkedList;
import java.util.Queue;

// Java program to convert binary tree into its mirror 

/* Class containing left and right child of current 
node and key value*/
class Node {
	int data;
	Node left, right;

	public Node(int item) {
		data = item;
		left = right = null;
	}
}

class BinaryTree {
	Node root;

	void mirror() {
		root = mirrorRecursive(root);
	}

	// recursive method to covert to mirror tree
	Node mirrorRecursive(Node node) {
		if (node == null)
			return node;

		/* do the subtrees */
		Node left = mirrorRecursive(node.left);
		Node right = mirrorRecursive(node.right);

		/* swap the left and right pointers */
		node.left = right;
		node.right = left;

		return node;
	}

	// iterative method to covert to mirror tree
	static void mirrorIterative(Node root) {
		if (root == null)
			return;

		Queue<Node> q = new LinkedList<>();
		q.add(root);

		// Do BFS. While doing BFS, keep swapping
		// left and right children
		while (q.size() > 0) {
			// pop top node from queue
			Node curr = q.peek();
			q.remove();

			// swap left child with right child
			Node temp = curr.left;
			curr.left = curr.right;
			curr.right = temp;
			;

			// push left and right children
			if (curr.left != null)
				q.add(curr.left);
			if (curr.right != null)
				q.add(curr.right);
		}
	}

	void inOrder() {
		inOrder(root);
	}

	/*
	 * Helper function to test mirror(). Given a binary search tree, print out
	 * its data elements in increasing sorted order.
	 */
	void inOrder(Node node) {
		if (node == null)
			return;

		inOrder(node.left);
		System.out.print(node.data + " ");

		inOrder(node.right);
	}

	/* testing for example nodes */
	public static void main(String args[]) {
		/* creating a binary tree and entering the nodes */
		BinaryTree tree = new BinaryTree();
		tree.root = new Node(1);
		tree.root.left = new Node(2);
		tree.root.right = new Node(3);
		tree.root.left.left = new Node(4);
		tree.root.left.right = new Node(5);

		/* print inorder traversal of the input tree */
		System.out.println("Inorder traversal of input tree is :");
		tree.inOrder();
		System.out.println("");

		/* convert tree to its mirror */
		tree.mirror();

		/* print inorder traversal of the minor tree */
		System.out.println("Inorder traversal of binary tree is : ");
		tree.inOrder();

	}
}
