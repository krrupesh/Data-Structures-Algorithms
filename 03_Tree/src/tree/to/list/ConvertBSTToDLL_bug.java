package tree.to.list;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class ConvertBSTToDLL_bug {

	
	Node prev;
	
	public static void main(String[] args) {

		BinarySearchTree bst = new BinarySearchTree();
		ConvertBSTToDLL_bug obj = new ConvertBSTToDLL_bug();
		Node head = null;
		Node tail = null;
		Node n = obj.converBSTToDLL(bst.root, head, tail);
		
		obj.displayDll(head);
		
		obj.displayDllTail(tail);		
	}
	
	public Node converBSTToDLL(Node root, Node head, Node tail){
		
		if(root == null){
			return null;
		}
		
		converBSTToDLL(root.left,head,tail);
		
		Node n = new Node(root.key);
		if(head == null){
			head = tail = n;
			prev = n;
		}else{
			prev.right = tail = n;
			n.left = prev;
		}
		prev = n;
		
		converBSTToDLL(root.right, head, tail);
		
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
