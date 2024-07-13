package geeks.bst;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class FindKthSmallest extends BinarySearchTree{
	int count;

	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();
		FindKthSmallest obj = new FindKthSmallest();
		
		System.out.println("InOrder traversal of tree");
		tree.inorder();
				
		obj.findKthSmallest(tree.root,3);
		
		obj.findKthLargest(tree.root,3);
		
		System.out.println("Completed ");
		
	}
	
	
    // This method mainly calls InorderRec()
    public void findKthSmallest(Node root,int k)  {
    	findKthSmallestRec(root, k);
    }
 
    // A utility function to do inorder traversal of BST
    public void findKthSmallestRec(Node root,int k) {
        if (root != null) {
            if (count < k) {
            	findKthSmallestRec(root.left, k);
				count++;
				System.out.println("count " + count);
				if (count == k) {
					System.out.print(k + ", smallest node : " + root.key + " ");
					return;
				}
				findKthSmallestRec(root.right, k);
			}else{
				return;
			}
        }
    }
    
    
	
    // This method mainly calls InorderRec()
    public void findKthLargest(Node root,int k)  {
    	count = 0;
    	findKthLargestRec(root, k);
    }
 
    // A utility function to do inorder traversal of BST
    public void findKthLargestRec(Node root,int k) {
        if (root != null) {
            if (count < k) {
            	findKthLargestRec(root.right, k);
				count++;
				System.out.println("count " + count);
				if (count == k) {
					System.out.print(k + ", largest node : " + root.key + " ");
					return;
				}
				findKthLargestRec(root.left, k);
			}else{
				return;
			}
        }
    }


}
