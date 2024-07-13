package geeks.bt;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class P_20_Root_To_leaf_path {

	public static void main(String[] args) {

		P_20_Root_To_leaf_path obj = new P_20_Root_To_leaf_path();
		int path [] = new int[1000];
		
		BinarySearchTree bst = new BinarySearchTree();
		//obj.findRootLeafPath(bst.root, path, 0);// 
		
		//obj.findRootNodePath(bst.root, path, 0, 40);
		
		obj.findRootNodePathBST(bst.root, path, 0, 40);

		
	}

	
	public void findRootLeafPath(Node root, int path[], int pathlen){
		System.out.println("pathlen : "+pathlen);
		if(root == null){
			return ;
		}
		
		path[pathlen] = root.key;
		pathlen++;
		
		if((root.left == null) && (root.right == null)){
			printArray(path,pathlen);
		}
		
		findRootLeafPath(root.left, path, pathlen);
		findRootLeafPath(root.right, path, pathlen);
		
	}

	public void findRootNodePath(Node root, int path[], int pathlen, int node){
		System.out.println("pathlen : "+pathlen);
		if(root == null){
			return ;
		}
		
		path[pathlen] = root.key;
		pathlen++;
		
		if((root.key == node)){
			printArray(path,pathlen);
			return;
		}
		
		findRootNodePath(root.left, path, pathlen, node);
		findRootNodePath(root.right, path, pathlen, node);
		
	}
	
	public void findRootNodePathBST(Node root, int path[], int pathlen, int node){
		System.out.println("pathlen : "+pathlen);
		if(root == null){
			return ;
		}
		
		path[pathlen] = root.key;
		pathlen++;
		
		if((root.key == node)){
			printArray(path,pathlen);
			return;
		}
		
		if(node < root.key){
			findRootNodePathBST(root.left, path, pathlen, node);
		}else{			
			findRootNodePathBST(root.right, path, pathlen, node);
		}
		
		
	}

	private void printArray(int[] path, int pathlen) {
		System.out.println();
		for(int i=0;i<pathlen;i++){
			System.out.print(path[i]+" ");
		}
	}
}
