package sept29.geeks.tree;


// https://www.geeksforgeeks.org/check-for-children-sum-property-in-a-binary-tree/

public class ChildrenSumProperty {

	Node root; 
	
	public static void main(String[] args) {

		ChildrenSumProperty tree = new ChildrenSumProperty(); 
        tree.root = new Node(10); 
        tree.root.left = new Node(8); 
        tree.root.right = new Node(2); 
        tree.root.left.left = new Node(3); 
        tree.root.left.right = new Node(5); 
        tree.root.right.right = new Node(3); 
        
        System.out.println(tree.isSumProperty(tree.root));
        
        if (tree.isSumProperty(tree.root) == 0) 
            System.out.println("The given tree satisfies children" + " sum property"); 
        else
            System.out.println("The given tree does not satisfy children" + " sum property"); 
				
	}

	private int isSumProperty(Node root) {

		if(root == null)
			return 0;
		
		if(root.left != null && root.right != null){
			if(!(root.data == root.left.data + root.right.data)){
				System.out.println("Step 1->" + root.data);
				return -1;
			}
		}else if(root.left != null && root.right == null){
			if(!(root.data == root.left.data)){
				System.out.println("Step 2->" + root.data);

				return -1;
			}
		}else if(root.left == null && root.right != null){
			if(!(root.data == root.right.data)){
				System.out.println("Step 3->" + root.data);

				return -1;
			}
		}
			
		isSumProperty(root.left);
		isSumProperty(root.right);

	return 0;	
	}

}
