package p.reverselinklist.recursion;

public class Node {

	Node next;
	int iData;
	
	public Node(int iData) {
		super();
		this.iData = iData;
	}
	
	public Node(Node next) {
		super();
		this.next = next;
	}

	public void displayLink(){
		System.out.print(this.iData+" ");
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}
	
	
}
