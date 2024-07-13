package google.medium;

import tree.basic.operations.BinarySearchTree;
import tree.basic.operations.Node;


/* Let us create following BST
	   	50
	  /    \
  	30      70
   /  \    /  \
  20  40  60   80
   \
   25	
  
*/
public class LeafNodesRecursivelyTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BinarySearchTree bst = new BinarySearchTree();
		
		findLeafNodesRec(bst.root, bst);
		//findLeafNodes(bst.root);
		System.out.println();
		//findLeafNodes(bst.root);
		
		//Steps
		/*
		 1. find all the leaf nodes 
		 2. delete the leaf nodes from tree 
		 3. call the method recursively 
		 
		 4. if i can store parent node reference then my job will be done
		 
		 
		 */

	}
	
	
	public static void findLeafNodesRec(Node root, BinarySearchTree bst){
		bst.inorderRec(root);
		System.out.println();

		findLeafNodes(root);
		System.out.println("Step-2 "+root);
		
		while(root.left != null && root.right != null){
			findLeafNodesRec(root, bst);
		}
		
	}
	
	public static Node findLeafNodes(Node root){
				
		if(root == null){
			return null; 
		}
		
		if(root != null && root.left == null && root.right == null){
			System.out.print(root.key+" ");
			return null;
		}

		root.left = findLeafNodes(root.left);
		root.right = findLeafNodes(root.right);
		
		return root;
	}

}
