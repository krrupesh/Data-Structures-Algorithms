package geeks.bst;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class P_20_TrepletInBST{

	public static void main(String[] args) {

		BinarySearchTree bst = new BinarySearchTree();
		
		System.out.println();
	}

    // A utility function to do reverseInOrder traversal of BST
    public void reverseInOrderRec(Node root) {
        if (root != null) {
        	reverseInOrderRec(root.right);
            //System.out.print(root.key+" ");
        	reverseInOrderRec(root.left);
        }
    }

    // A utility function to do inorder traversal of BST
    public void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key+" ");
            inorderRec(root.right);
        }
    }
}
