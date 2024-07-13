package basic.singly.linkedlist;

public class Node {
   
	public int data;
	public Node next;
	
	public Node(int data){
	  this.data=data;
	}
	
	public void getData(){
		System.out.print(data+" ");
	}
	
	public int getData1(){
		
	 return data;	
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}
}
