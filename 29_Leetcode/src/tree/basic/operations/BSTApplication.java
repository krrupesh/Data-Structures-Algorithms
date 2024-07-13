package tree.basic.operations;

public class BSTApplication {

	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();		

        /* Let us create following BST
              50
           /     \
          30      70
         /  \    /  \
       20   40  60   80 */
     
		//tree = createBinarySearchTree();
		
		
        // print inorder traversal of the BST
        // tree.inorder();
		tree.preOrder();
         
         // print rnl order traversal of the BST
         //tree.rnlOrder();
        
        //tree.preOrder();
        
        boolean found = false;
        //found = tree.serchKey(tree.root, 70);
        
        /*
        if(found){
        	System.out.println("Node is found");
        }else{
        	System.out.println("Node is not found");
        }
        */
		
	}

}
