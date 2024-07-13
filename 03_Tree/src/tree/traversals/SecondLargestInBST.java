package tree.traversals;

import java.util.ArrayList;
import java.util.List;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class SecondLargestInBST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SecondLargestInBST obj = new SecondLargestInBST();
		/* Let us create following BST
	        50
	     /     \
	    30      70
	   /  \    /  \
	 20   40  60   80 */

	BinarySearchTree tree = new BinarySearchTree();
	
	//tree.insert(10);
	//tree.insert(42);
	
	inorder(tree.root);
	
	
	reverseInorder(tree.root);


	int k = 3;
	findSecondLargest(tree.root, k);
	
	System.out.println("\npredecessor ");

	}
	
	
	public static void findSecondLargest(Node root, int k){
		System.out.println("kth Largest element");
		List<Integer> list = new ArrayList();
		findSecondLargest(root, list);
		
		System.out.println(list);
		System.out.println(list.get(k-1));
		
		
		
	}
	
	public static void findSecondLargest(Node root, List<Integer> list){
		if (root != null) {
			findSecondLargest(root.right, list);
            list.add(root.key);
            findSecondLargest(root.left, list);
        }
	}
	
	
    // This method mainly calls InorderRec()
    public static void inorder(Node root)  {
    	System.out.println("\nInOrder traversal is!");
       inorderRec(root);
       System.out.println("\n");
    }
 
    // A utility function to do inorder traversal of BST
    public static void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key+" ");
            inorderRec(root.right);
        }
    }
    
    
    
    // This method mainly calls InorderRec()
    public static void reverseInorder(Node root)  {
    	System.out.println("\nReverse InOrder traversal is!");
    	reverseInorderRec(root);
       System.out.println("\n");
    }
 
    // A utility function to do inorder traversal of BST
    public static void reverseInorderRec(Node root) {
        if (root != null) {
        	reverseInorderRec(root.right);
            System.out.print(root.key+" ");
            reverseInorderRec(root.left);
        }
    }

}
