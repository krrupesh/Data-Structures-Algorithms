package tree.views;

import basic.operations.BinarySearchTree;
import basic.operations.Node;


/* Let us create following BST
      50
   /     \
  30      70
 /  \    /  \
20   40  60   80 */

public class PrintLeftView {

	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();	
		tree.inorder();

		printLeftViewOfTree(tree.root);
		
		tree.insert(25);
		
		tree.inorder();

		printLeftViewOfTree(tree.root);		
	}
	
	public static void printLeftViewOfTree(Node root){
		if(root == null){
			return;
		}
		
		System.out.print(root.key + " ");
		
		if(root.left != null){
			printLeftViewOfTree(root.left);
		}else if(root.right != null){
			printLeftViewOfTree(root.right);
		}
		
	}
}
