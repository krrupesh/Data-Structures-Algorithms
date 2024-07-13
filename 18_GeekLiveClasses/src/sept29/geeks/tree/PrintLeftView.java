package sept29.geeks.tree;

public class PrintLeftView {

	Node root;
	
	/*
	 * 
                   10
                 /   \
	           8       2
	         /  \       \
	        3    5       2
	
	
	 * 
	 */
	
	public static void main(String[] args) {

		PrintLeftView tree = new PrintLeftView(); 
        tree.root = new Node(10); 
        tree.root.left = new Node(8); 
        tree.root.right = new Node(2); 
        tree.root.left.left = new Node(3); 
        tree.root.left.left.right = new Node(4); 

        tree.root.left.right = new Node(5); 
        tree.root.right.right = new Node(2); 
        
        printPath(tree.root);		
	}
	
	// this wont work for all cases
	static void printPath(Node root){
		
		if(root == null){
			return;
		}
		
		System.out.println(root.data);
		
		if(root.left != null){
			printPath(root.left);
		}else{
			printPath(root.right);
		}		
		
	}
	
	
	static void printLeftView(Node root){
		
	}
	
	
	
	static void printTopView(Node root){
		
	}
	
	
	static void printBottomView(Node root){
		
	}
	

}
