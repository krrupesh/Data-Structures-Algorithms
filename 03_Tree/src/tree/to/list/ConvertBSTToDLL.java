package tree.to.list;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class ConvertBSTToDLL {

	static Node head, tail;
	Node prev;
	
	public static void main(String[] args) {

		BinarySearchTree bst = new BinarySearchTree();
		ConvertBSTToDLL obj = new ConvertBSTToDLL();
		
		Node n = obj.converBSTToDLL(bst.root);
		
		obj.displayDll(head);
		
		obj.displayDllTail(tail);	
		
		bst.inorder();
	}
	
	public Node converBSTToDLL(Node root){
		
		if(root == null){
			return null;
		}
		
		converBSTToDLL(root.left);
		
		Node n = new Node(root.key);
		if(head == null){
			head = tail = n;
		}else{
			prev.right = tail = n;// you need to have two
			n.left = prev;  // pointers head and tail thats why
		}
		prev = n;
		
		converBSTToDLL(root.right);
		
		return n;
	}
	
	public void displayDll(Node n){
		System.out.println("Traversal using head !");
		while(n != null){
			System.out.print(n.key+" ");
			n = n.right;
		}
	}
	
	public void displayDllTail(Node n){
		System.out.println("\nTraversal using tail !");
		while(n != null){
			System.out.print(n.key+" ");
			n = n.left;
		}
	}

}
