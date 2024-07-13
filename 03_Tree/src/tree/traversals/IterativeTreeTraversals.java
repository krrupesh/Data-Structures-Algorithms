package tree.traversals;

import basic.operations.BinarySearchTree;

public class IterativeTreeTraversals {

	public static void main(String[] args) {
		BinarySearchTree tree = new BinarySearchTree();
		tree.inorderIterative(tree.root);
	}

}
