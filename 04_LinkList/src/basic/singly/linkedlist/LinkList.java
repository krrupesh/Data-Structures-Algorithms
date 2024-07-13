package basic.singly.linkedlist;

public class LinkList {

	public Node head;

	public void insertFirst(int data) {
		Node newLink = new Node(data);

		if (isEmpty()) {
			head = newLink;
		} else {
			newLink.next = head;
			head = newLink;
		}
	}

	public void insertAfter(int data, int after) {
		Node newLink = new Node(data);
		Node current = head;
		if (isEmpty()) {
			head = newLink;
		}

		while (current.data != after) {
			current = current.next;
		}
		newLink.next = current.next;
		current.next = newLink;
		System.out.println("item inserted");
	}

	public void insertLast(int data) {
		Node newLink = new Node(data);
		Node current = head;
		if (isEmpty()) {
			head = newLink;
		}

		while (current.next != null) {
			current = current.next;
		}
		current.next = newLink;
	}

	public void displayList(Node head) {
		Node current = head;

		while (current != null) {
			current.getData();
			current = current.next;
		}
	}

	public boolean isEmpty() {

		return head == null;
	}

}
