package sept14.geeks.linkedlist;

public class MyLinkedList {

	
	static Node head;

	public static void main(String[] args) {

		Node head1 = insertFirst(10);	
		head1 = insertFirst(20);	
		head1 = insertFirst(30);	
		head1 = insertFirst(40);	

	}
	
	
	public static Node insertFirst(int value){
				
		if(head == null){
			head = new Node(value);
		}
		
		Node tmp = new Node(value);
		
		tmp.next = head;
		head = tmp;
				
		return head;	
	}

	
	
	public Node reverseLinkedList(){
		
		
		return null;
	}
	
	
	
	public Node reverseLinkedListRecursive(){
		
		
		return null;
	}
	
	
	
	
	
	
}
