package tree.construct;

import tree.traversals.FindInOrderSuccesorAndPredecessor;
import basic.operations.BSTApplication;
import basic.operations.BinarySearchTree;
import basic.operations.Node;

public class FromInOrderAndPreOrder {

	static int preO[] = {50,30,20,40,70,60,80};

	public static void main(String[] args) {

		FindInOrderSuccesorAndPredecessor obj = new FindInOrderSuccesorAndPredecessor();
		/* Let us create following BST
	        50
	     /     \
	    30      70
	   /  \    /  \
	 20   40  60   80 */

	//BinarySearchTree tree = BSTApplication.createBinarySearchTree();
	
	//tree.insert(10);
	//tree.insert(42);
	
	// 20 30 40 50 60 70 80 [LNR]
	//tree.inorder();
	
	// 50 30 20 40 70 60 80 [NLR]
	//tree.preOrder();
	
	createBinaryTree();
			
	}


	public static  void createBinaryTree(){
		int inO[] = {20,30,40,50,60,70,80};
		
		Node root = null;
		BinarySearchTree tree = new BinarySearchTree();
		tree.root = root;

		create(inO,0,inO.length,"root",root);
		
		System.out.println("Newly Constrcted tree");
		tree.inorderRec(root);
	}
	
	static int i = 0;
	
	
	public static void create(int [] inO,int startindex,int endindex,String child,Node root){
		if (root !=null) {
			System.out.println("preO["+i+"] "+preO[i]+" startindex " + startindex + " endindex "+ endindex + " child " + child + " root : " + root.key);
		}else{
			System.out.println("root is null");
		}
		int index = getIndex(preO[i],inO,startindex,endindex);
		i++;
		
		//System.out.println("index : "+index);
		
		if(index <= 0){
			return;
		}
		
		if(child.equals("root")){
			root = new Node(inO[index]);
		}
		
		System.out.println("index : "+index+" element : "+inO[index]);

		
		if (child.equals("left")) {
			System.out.println("setting left child start root "+root.key);
			root = root.left = new Node(inO[index]);
			//root.setLeftNode(new Node(inO[index]));
			System.out.println("setting left child end root "+root.key);
		}

		create(inO, 0, index-1,"left",root);
		
		if (child.equals("right")) {
			root = root.right = new Node(inO[index]);
			//root.setRightNode(new Node(inO[index]));
		}
		
		create(inO, index+1,endindex,"right",root);
		
		//return -1;
	}
	
	public static int getIndex(int preElement, int []inO,int startindex,int endindex){
		
		for(int i=startindex;i<endindex;i++){
			if(inO[i]==preElement){
				return i;
			}
		}
		
		if(startindex == endindex){
			if(inO[endindex]==preElement){
				return endindex;
			}
		}
		
		return -1;
	}
	
}
