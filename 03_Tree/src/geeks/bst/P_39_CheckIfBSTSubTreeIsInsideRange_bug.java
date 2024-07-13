package geeks.bst;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class P_39_CheckIfBSTSubTreeIsInsideRange_bug extends BinarySearchTree{

	static int count;
	int minRange = 20;
	int maxRange = 60;
	
	Node left,right;
	
	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();
		P_39_CheckIfBSTSubTreeIsInsideRange_bug obj = new P_39_CheckIfBSTSubTreeIsInsideRange_bug();
				
		
		obj.checkTreeInsideRange(tree.root);
		
		System.out.println("count : "+count);
	}

	public Node checkTreeInsideRange(Node root){		

		left = checkTreeInsideRange(root.left);		
		
		if((left != null)){
			if(checkInsideRange(root.key)){
				count++;
				root.isInsideRange = true;
				System.out.println("when left & right null node : "+root.key);
			}
			return null;
		}
		
		right = checkTreeInsideRange(root.right);
		
		if(((right == null))){
			if(checkInsideRange(root.key)){
				count++;
				root.isInsideRange = true;
				System.out.println("when left & right null node : "+root.key);
			}
			return null;
		}
		
		
		if((left == null) & (right == null)){
			if((left.isInsideRange) & (right.isInsideRange) & (checkInsideRange(left.key)) & (checkInsideRange(right.key))){
				count++;
				root.isInsideRange = true;
				System.out.println("Node inside range : "+root.key);

			}
		}

		return root;
	}
	
	public boolean checkInsideRange(int key){
		if((minRange <= key) && (maxRange >= key)){
			return true;
		}else{
			return false;
		}
	}
}
