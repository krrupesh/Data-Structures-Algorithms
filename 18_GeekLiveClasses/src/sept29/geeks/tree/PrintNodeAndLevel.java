package sept29.geeks.tree;

public class PrintNodeAndLevel {

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

		PrintNodeAndLevel tree = new PrintNodeAndLevel(); 
        tree.root = new Node(10); 
        tree.root.left = new Node(8); 
        tree.root.right = new Node(2); 
        tree.root.left.left = new Node(3); 
        tree.root.left.right = new Node(5); 
        tree.root.right.right = new Node(2); 
        
       // printNodeAndLevel(tree.root, 0);	
        
        System.out.println(getLevel(tree.root,3, 0));
	}
	
	static void printNodeAndLevel(Node root, int level){
	
		if(root == null){
			return;
		}
		
		System.out.println(root.data+" "+level);
		
		printNodeAndLevel(root.left, level+1);
		printNodeAndLevel(root.right, level+1);		
	}
	
	// fix this code
	static int getLevel(Node root, int key, int level){
		
		if(root == null){
			return -1;
		}
		
		if(root.data == key){
			return level;
		}
		
		getLevel(root.left,key, level+1);
		getLevel(root.right,key, level+1);		
		
		return level;
	}

}
