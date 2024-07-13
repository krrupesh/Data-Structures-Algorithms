package tree.mirror.check;
// Java program to see if two trees 

// are mirror of each other 

// A binary tree node 
class Node {
	int data;
	Node left, right;

	public Node(int data) {
		this.data = data;
		left = right = null;
	}
}

class BinaryTree {
	Node a, b;

	/*
	 * Given two trees, return true if they are mirror of each other
	 */
	boolean areMirror(Node a, Node b) {
		/* Base case : Both empty */
		if (a == null && b == null)
			return true;

		// If only one is empty
		if (a == null || b == null)
			return false;

		/*
		 * Both non-empty, compare them recursively Note that in recursive
		 * calls, we pass left of one tree and right of other tree
		 */
		return a.data == b.data && areMirror(a.left, b.right) && areMirror(a.right, b.left);
	}

	boolean isMirror(Node node1, Node node2) {
		// if both trees are empty,
		// then they are mirror image
		if (node1 == null && node2 == null)
			return true;

		// For two trees to be mirror images, the following
		// three conditions must be true 1 - Their root
		// node's key must be same 2 - left subtree of left
		// tree and right subtree
		// of right tree have to be mirror images
		// 3 - right subtree of left tree and left subtree
		// of right tree have to be mirror images
		if (node1 != null && node2 != null && node1.data == node2.data)
			return (isMirror(node1.left, node2.right) 
				 && isMirror(node1.right, node2.left));

		// if none of the above conditions is true then
		// root1 and root2 are not mirror images
		return false;
	}

	// Driver code to test above methods
	public static void main(String[] args) {
		BinaryTree tree = new BinaryTree();
		Node a = new Node(1);
		Node b = new Node(1);
		a.left = new Node(2);
		a.right = new Node(3);
		a.left.left = new Node(4);
		a.left.right = new Node(5);

		b.left = new Node(3);
		b.right = new Node(2);
		b.right.left = new Node(5);
		b.right.right = new Node(4);

		if (tree.areMirror(a, b) == true)
			System.out.println("Yes");
		else
			System.out.println("No");

	}
}

// This code has been contributed by Mayank Jaiswal(mayank_24)
