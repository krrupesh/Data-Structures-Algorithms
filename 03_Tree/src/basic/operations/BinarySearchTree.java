package basic.operations;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinarySearchTree {
	
    // Root of BST
    public Node root;
 
    // Constructor
    public BinarySearchTree() { 
        root = null; 
        
	    insert(50);
        insert(30);
        insert(20);
        insert(40);
        insert(70);
        insert(60);
        insert(80);
    }
    
    
    //TODO Optimise it
    public boolean serchKey(Node root, int key){
    	
    	if(root == null){
        	System.out.println("BinarySearchTree.serchKey()  Node data : "+root);
    		return false;
    	}else{
        	System.out.println("BinarySearchTree.serchKey()  Node data : "+root.key);
    	}
    	
    	if(root.key == key){
    		return true;
    	}
    	
    	return serchKey(root.left, key) || serchKey(root.right,key);
    }
	 
    // This method mainly calls insertRec()
    public void insert(int key) {
       root = insertRec(root, key);
       //System.out.println("BinarySearchTree.insert() root : "+root.key);
    }
     
    /* A recursive function to insert a new key in BST */
    public Node insertRec(Node root, int key) {
 
        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }
 
        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);
 
        /* return the (unchanged) node pointer */
        return root;
    }
 
    // This method mainly calls InorderRec()
    public void inorder()  {
    	System.out.println("\nInOrder traversal is!");
       inorderRec(root);
       System.out.println("\n");
    }
 
    // A utility function to do inorder traversal of BST
    public void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key+" ");
            inorderRec(root.right);
        }
    }
    
    // A utility function to do inorder traversal of BST
    public void inorderIterative(Node root) {
    	
    	Node current = root;
    	Stack<Node> s = new Stack<Node>();
    	
        while(!s.isEmpty() || current != null) {
        	
            while(current != null){
            	s.push(current);
                current = current.left;
            }
            
        	current = s.pop();
        	System.out.print(current.key +" ");
        	current = current.right;          
        }
    }
    
    // This method mainly calls preOrderRec()
    public void preOrder()  {
    	preOrderRec(root);
    }
 
    // A utility function to do preOrder traversal of BST
    public void preOrderRec(Node root) {
        if (root != null) {
            System.out.print(root.key+" ");
        	preOrderRec(root.left);
            preOrderRec(root.right);
        }
    }
    
    // This method mainly calls postOrderRec()
    public void postOrder()  {
    	postOrderRec(root);
    }
 
    // A utility function to do postOrder traversal of BST
    public void postOrderRec(Node root) {
        if (root != null) {
        	postOrderRec(root.left);
            postOrderRec(root.right);
            System.out.print(root.key+" ");
        }
    }

    // This method mainly calls postOrderRec()
    public void reverseInOrder()  {
    	System.out.println();
    	reverseInOrderRec(root);
    }
 
    // A utility function to do postOrder traversal of BST
    public void reverseInOrderRec(Node root) {
        if (root != null) {
        	reverseInOrderRec(root.right);
            System.out.print(root.key+" ");
        	reverseInOrderRec(root.left);
        }
    }
    
    
	public void printLevelOrder(){
		
		Node root = this.root;
		Queue<Node> q = new LinkedList<Node>();
		
		q.offer(root);
		q.offer(null);
		
		while(!q.isEmpty()){
			
			Node n = q.poll();
						
			if(n != null){
				System.out.print(n.key+" ");
				
				if(n.left != null){
					q.offer(n.left);
				}
				
				if(n.right != null){
					q.offer(n.right);
				}
			}else{
				if(!q.isEmpty()){
					q.offer(null);
				}
				System.out.println();
			}
			
		}
	}
}
// This code is contributed by Ankur Narain Verma

