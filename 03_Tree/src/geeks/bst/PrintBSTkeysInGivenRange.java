package geeks.bst;

import basic.operations.BSTApplication;
import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class PrintBSTkeysInGivenRange {

		    /* Let us create following BST
		    50
		 /     \
		30      70
		/  \    /  \
	  20   40  60   80 */
	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();
		PrintBSTkeysInGivenRange obj = new PrintBSTkeysInGivenRange();
		obj.inorder(tree.root,30,50);
		
		System.out.println("Completed ");		
	}	
	
    // This method mainly calls InorderRec()
    public void inorder(Node root,int k1, int k2)  {
       inorderRec(root, k1,k2);
    }
 
    // A utility function to do inorder traversal of BST
    public void inorderRec(Node root,int k1, int k2) {
        if (root != null) {
            if ((root.key>=k1)&&(root.key <= k2)) {
				inorderRec(root.left, k1,k2);
				if ((root.key>=k1)&&(root.key <= k2)) {
					System.out.println("node : " + root.key + " ");
				}
				inorderRec(root.right, k1,k2);
			}else{
				return;
			}
        }
    }

}
