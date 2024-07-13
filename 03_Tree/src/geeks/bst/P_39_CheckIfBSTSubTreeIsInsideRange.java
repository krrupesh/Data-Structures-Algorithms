package geeks.bst;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class P_39_CheckIfBSTSubTreeIsInsideRange extends BinarySearchTree{

	static int count;
	int minRange = 20;
	int maxRange = 10;
	boolean left,right;
	
	public static void main(String[] args) {

		BinarySearchTree tree = new BinarySearchTree();
		tree.insert(100);
		P_39_CheckIfBSTSubTreeIsInsideRange obj = new P_39_CheckIfBSTSubTreeIsInsideRange();
				
		
		obj.checkTreeInsideRange(tree.root);
		
		System.out.println("count : "+count);
	}

	public boolean checkTreeInsideRange(Node root){
		
		boolean l = root.left!=null ? checkTreeInsideRange(root.left):true;		
		boolean r = root.right!=null ? checkTreeInsideRange(root.right):true;		
		
		if( l & r & checkInsideRange(root.key)){
			System.out.println(root.key);
			++count;
			return true;
		}
		
	 return false;
	}
	
	public boolean checkInsideRange(int key){
		if((minRange <= key) && (maxRange >= key)){
			return true;
		}else{
			return false;
		}
	}
}
