package tree.views;

import basic.operations.BinarySearchTree;
import basic.operations.Node;


/* Let us create following BST
      50
   /     \
  30      70
 /  \    /  \
20   40  60   80 */

public class PrintRightView {

	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();	
		tree.inorder();

		printRightViewOfTree(tree.root);
		
		tree.insert(75);
		
		tree.inorder();

		printRightViewOfTree(tree.root);
		
	}
	
	public static void printRightViewOfTree(Node root){
		if(root == null){
			return;
		}
		
		System.out.print(root.key + " ");

		
		if(root.right != null){
			printRightViewOfTree(root.right);
		}else if(root.left != null){
			printRightViewOfTree(root.left);
		}
		
	}
}
