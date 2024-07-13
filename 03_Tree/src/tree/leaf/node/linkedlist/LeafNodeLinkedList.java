package tree.leaf.node.linkedlist;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class LeafNodeLinkedList {

	public static void main(String[] args) {

		BinarySearchTree bst = new BinarySearchTree();
		
		bst.insert(100);
		bst.insert(90);
		bst.insert(110);
		
		bst.printLevelOrder();
				
		Node head = inorderRec(bst.root);
		
		System.out.println("\n head : "+head.key);
		
		displayLinkedist(head);
		
	}
	
	static Node prevNode = null;
	static Node head = null;
 
    public static Node inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            if(root.isLeafNode()){
            	if(prevNode != null){
            		prevNode.right = root;
            	}else{
            		head = root;
            	}
                prevNode = root;
            }
            
            inorderRec(root.right);
        }
		return head;
    }
    
    static void displayLinkedist(Node head){
    	Node current = head;
    	
    	while(current != null){
    		
    		System.out.println(current.key);
    		current = current.right;
    		
    	}
    }

}
