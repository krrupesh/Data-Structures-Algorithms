package tree.levelorder.traversal;

import java.util.LinkedList;
import java.util.Queue;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class TreeLevelOrderTraversal {

	public static void main(String[] args) {

		TreeLevelOrderTraversal obj = new TreeLevelOrderTraversal();
		BinarySearchTree bst = new BinarySearchTree();
		bst.insert(100);
		bst.insert(90);
		bst.insert(110);

		
		obj.printLevelOrder(bst.root);
		
		//obj.printLevelOrderToFromLevel(bst.root, 3, 5);
	}

	public void printLevelOrder(Node root){
		
		Queue<Node> q = new LinkedList<Node>();
		
		q.offer(root);
		q.offer(null);
		
		while(!q.isEmpty()){
			
			Node n = q.poll();
						
			if(n != null){
				System.out.print(n.key+" ");
				
				if(n.left != null){
					q.offer(n.left);
				}
				
				if(n.right != null){
					q.offer(n.right);
				}
			}else{
				if(!q.isEmpty()){
					q.offer(null);
				}
				System.out.println();
			}
			
		}
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
