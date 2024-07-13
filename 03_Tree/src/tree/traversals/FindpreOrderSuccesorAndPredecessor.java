package tree.traversals;

import basic.operations.BSTApplication;
import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class FindpreOrderSuccesorAndPredecessor {

	public static void main(String[] args) {
		
		FindpreOrderSuccesorAndPredecessor obj = new FindpreOrderSuccesorAndPredecessor();
			/* Let us create following BST
		        50
		     /     \
		    30      70
		   /  \    /  \
		 20   40  60   80 */

		BinarySearchTree tree = new BinarySearchTree();
		
		//tree.insert(10);
		//tree.insert(42);
		
		tree.preOrder();
		
		
		//obj.findpreOrderSuccesorRec(tree.root,20);
		
		System.out.println("\npredecessor ");
		obj.findpreOrderPredecessorRec(tree.root,40);
		
	}
	
	
    boolean found = false;
 
    public  void findpreOrderSuccesorRec(Node root,int value) {
        if (root != null) {
            
            if(found){
                System.out.print("\nsuccessor : "+root.key+" ");
                found = false;// why it was required
                return;
            }
            
            if(root.key == value){
            	found = true;
            }
            
            findpreOrderSuccesorRec(root.left,value);
            
            findpreOrderSuccesorRec(root.right,value);
        }
    }
    
    int preValue = Integer.MIN_VALUE;
    public  void findpreOrderPredecessorRec(Node root,int value) {
        if (root != null) {
                       
            if(root.key == value){
               System.out.print(preValue);            
            }
            
            preValue = root.key;
            
            findpreOrderPredecessorRec(root.left,value);

            findpreOrderPredecessorRec(root.right,value);
        }
        
    }

}
