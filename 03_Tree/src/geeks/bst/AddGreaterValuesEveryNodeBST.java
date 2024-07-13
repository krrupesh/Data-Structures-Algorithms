package geeks.bst;

import basic.operations.BSTApplication;
import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class AddGreaterValuesEveryNodeBST extends BinarySearchTree{

	int sum = 0;

	public static void main(String[] args) {
		
		BinarySearchTree tree = new BinarySearchTree();		
		AddGreaterValuesEveryNodeBST obj  = new AddGreaterValuesEveryNodeBST();

        /* Let us create following BST
              50
           /     \
          30      70
         /  \    /  \
       20   40  60   80 */
		
	   obj.addGreaterValues();
		
	   obj.inorder();
     
	}

	/*
80 150 210 260 300 330 350 
InOrder traversal is!
350 330 300 260 210 150 80 
	 */
	
    // This method mainly calls postOrderRec()
    public void addGreaterValues()  {
    	System.out.println();
    	addGreaterValuesRec(root);
    }
 
    // A utility function to do postOrder traversal of BST
    public void addGreaterValuesRec(Node root) {
        if (root != null) {
        	addGreaterValuesRec(root.right);
        	sum = sum + root.key;
        	root.key = sum;
            System.out.print(root.key+" ");
            addGreaterValuesRec(root.left);
        }
    }
}
