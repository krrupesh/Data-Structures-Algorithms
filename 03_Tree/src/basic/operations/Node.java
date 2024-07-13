package basic.operations;

/* Class containing left and right child of current node and key value*/
public class Node {
	
    public int key;
    public Node left;
	public Node right;
    public boolean isInsideRange; // introduced for problem 39(BST) 
    
    public boolean isVisited; // introduced for problem FindRootToLeafPath.java


    public Node(int item) {
        key = item;
        left = right = null;
    }

	public Node getLeftNode() {
		return left;
	}

	public void setLeftNode(Node node) {
		this.left = node;
	}

	public Node getRightNode() {
		return right;
	}

	public void setRightNode(Node node) {
		this.right = node;
	}
    
    public boolean isLeafNode(){
    	return (this.left == null) && (this.right == null);
    }

	@Override
	public String toString() {
		return "Node [key=" + key + "]";
	}
    
    
}
