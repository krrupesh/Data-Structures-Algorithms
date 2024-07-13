package tree.traversals;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class FindInOrderSuccesorAndPredecessor {

	public static void main(String[] args) {
		
		FindInOrderSuccesorAndPredecessor obj = new FindInOrderSuccesorAndPredecessor();
			/* Let us create following BST
		        50
		     /     \
		    30      70
		   /  \    /  \
		 20   40  60   80 */

		BinarySearchTree tree = new BinarySearchTree();
		
		//tree.insert(10);
		//tree.insert(42);
		
		tree.inorder();
		
		
		//obj.findInOrderSuccesorRec(tree.root,20);
		
		System.out.println("\npredecessor ");
		obj.findInOrderPredecessorRec(tree.root,50);
		
	}
	
	
    boolean found = false;
 
    public  void findInOrderSuccesorRec(Node root,int value) {
        if (root != null) {
            findInOrderSuccesorRec(root.left,value);
            
            if(found){
                System.out.print("\nsuccessor : "+root.key+" ");
                found = false;// why it was required
                return;
            }
            
            if(root.key == value){
            	found = true;
            }
            
            findInOrderSuccesorRec(root.right,value);
        }
    }
    
    int preValue = Integer.MIN_VALUE;
    public  void findInOrderPredecessorRec(Node root,int value) {
        if (root != null) {
        	findInOrderPredecessorRec(root.left,value);
                       
            if(root.key == value){
               System.out.print(" "+preValue);            
            }
            
            preValue = root.key;
            
             findInOrderPredecessorRec(root.right,value);
        }
        
        //return preValue;  
    }

}
