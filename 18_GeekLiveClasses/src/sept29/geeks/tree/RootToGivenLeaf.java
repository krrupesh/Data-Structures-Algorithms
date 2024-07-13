package sept29.geeks.tree;

public class RootToGivenLeaf {

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

		RootToGivenLeaf tree = new RootToGivenLeaf(); 
        tree.root = new Node(10); 
        tree.root.left = new Node(8); 
        tree.root.right = new Node(2); 
        tree.root.left.left = new Node(3); 
        tree.root.left.right = new Node(5); 
        tree.root.right.right = new Node(2); 
        
        printPath(tree.root, 3);		
	}
	
	static boolean printPath(Node root, int leaf){
		
		if(root == null){
			return false;
		}
		
		if(root.data == leaf){
			System.out.println(root.data);
			return true;
		}
		
		if(printPath(root.left, leaf) || printPath(root.right, leaf)){
			System.out.println(root.data);
			return true;
		}
		return false;
		
	}
	

}
