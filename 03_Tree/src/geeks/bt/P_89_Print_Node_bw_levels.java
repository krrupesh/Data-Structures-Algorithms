package geeks.bt;

import java.util.LinkedList;
import java.util.Queue;

import tree.levelorder.traversal.TreeLevelOrderTraversal;
import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class P_89_Print_Node_bw_levels {

	public static void main(String[] args) {

		P_89_Print_Node_bw_levels obj = new P_89_Print_Node_bw_levels();
		BinarySearchTree bst = new BinarySearchTree();
		bst.insert(100);
		bst.insert(90);
		bst.insert(110);

		
		//obj.printLevelOrder(bst.root);
		
		obj.printLevelOrderToFromLevel(bst.root, 3, 5);
	}

	public void printLevelOrderToFromLevel(Node root, int from, int to){
		
		Queue<Node> q = new LinkedList<Node>();
		int count = 1;
		
		q.offer(root);
		q.offer(null);
		
		while(!q.isEmpty()){
			
			Node n = q.poll();
						
			if(n != null){
				
				if(from <= count && from <= to){
					System.out.print(n.key+" ");
				}
				
				if(n.left != null){
					q.offer(n.left);
				}
				
				if(n.right != null){
					q.offer(n.right);
				}
			}else{
				if(!q.isEmpty()){
					q.offer(null);
					count++;
				}
				System.out.println();
			}
			
		}
	}
	
}
