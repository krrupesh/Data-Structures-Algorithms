package p.reverselinklist.recursion;

public class LinkList {

	Node head;
	
	public LinkList(Node first) {
		super();
		this.head = first;
	}

	public LinkList() {
		// TODO Auto-generated constructor stub
	}

	public void insertFirst(int data){
		Node newLink = new Node(data);
		
		if(head == null){
			head = newLink;
		}else{
			newLink.next = head;
			head = newLink;
		}
	}
	
	public void displayLinkList(){
		Node current = this.head;
		
		while(current != null){
			current.displayLink();
			current = current.next;
		}
	}
	
	public Node reverseWithoutRecursion(){
		
		Node current = this.head;
		Node previousNode = null;
		
		while (current != null) {
			Node nextNode = current.getNext(); // storing the next node reference
			
			current.setNext(previousNode);
			
			// will be used in next iteration
			previousNode = current;
			current = nextNode;
		}
		
		return this.head=previousNode;
	}
	
	
	Node reverse(Node head){
		Node p = null;
		Node q = null;
		Node r = null;
			
		p = head;
		q = head;
		r = head;
		
		while(q!=null){
			if(q.next != null){
				r = q;
				q.next = p;
				
			}
			
			p = q;
			q = r;
		}		
		return p;
	}
	
	
	public void reverseUsingRecursion(){
		System.out.println();
		Node head = this.head;
		Node current = head;
		
		reverseUsingRecursion(head,current);
	}
	
	public void reverseUsingRecursion(Node head, Node current){
		System.out.println("head : "+head.iData+" ,current : "+current.iData);
		
		Node nextNode = current.getNext();
		if(nextNode == null){
			this.head = current;
			return;
		}
				
	    reverseUsingRecursion(head,nextNode);
	    
		nextNode.setNext(current);
	    
		if(current == head){
			current.setNext(null);
		}
	}
		
	//TODO
	public void printListFromBothSides(Node head){
		
		if(head == null){
			return;
		}
		
		System.out.print(head.iData+" ,");

		printListFromBothSides(head.next);
		
		System.out.print(head.iData+" ");
	}
	
}
