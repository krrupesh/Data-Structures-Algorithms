package basic.singly.linkedlist;

import java.util.LinkedList;

public class LinkListApp {

	public static void main(String[] args) {

		/*
		 * LinkedList L=new LinkedList();
		 * 
		 * L.insertFirst(10); L.insertFirst(20); L.insertFirst(30);
		 * L.insertFirst(40); L.insertFirst(50);
		 * 
		 * L.insertLast(111); L.insertLast(222);
		 * 
		 * L.insertAfter(1000, 30);
		 * 
		 * L.displayList();
		 */

		LinkList list = createLinkedList();

		// LinkList listR = removeGreaterValue(list, 40);

		// listR.displayList();

		// deleteNode(list, 50).displayList();
		
		
		Node head = reverseLinkedList(list.head);
		list.displayList(head);

	}

	public static Node reverseLinkedList(Node head){
		System.out.println();
		Node Prev = null;
		Node current = head;
		Node Next = current.next;
		
		while(current != null){
			Next = current.next;
			current.next = Prev;
			Prev = current;
			current = Next;
		}
		return Prev;
	}
	
	public static LinkList removeGreaterValue(LinkList list, int x) {

		Node current = null;
		Node prev = null;

		if (list.head != null) {
			if (list.head.data > x) {
				list.head = list.head.next;
			}

			prev = list.head;

			current = prev.next;
		}

		while (current != null) {

			int data = current.data;

			if (data > x) {
				System.out.println("---" + data);

				// prev = current.next;
				// current = current.next.next;
				current = current.next;

			} else {
				// prev = current;
				current = current.next;
			}
		}

		return list;
	}

	public static LinkedList delete(LinkList list, int data) {
		LinkedList<Integer> listL = new LinkedList<Integer>();
		Node current = list.head;

		while (current != null) {

			if (current.data > data) {
				listL.add(current.data);
			}
			current = current.next;

		}
		return listL;
	}

	public static void del(LinkedList<Integer> listL, LinkList list) {
		for (int key : listL) {
			LinkList list1 = deleteNode(list, key);
		}
	}

	static LinkList deleteNode(LinkList list, int key) {
		// Store head node
		Node temp = list.head, prev = null;

		// If head node itself holds the key to be deleted
		if (temp != null && temp.data == key) {
			list.head = temp.next; // Changed head
			return list;
		}

		// Search for the key to be deleted, keep track of the
		// previous node as we need to change temp.next
		while (temp != null && temp.data != key) {
			prev = temp;
			temp = temp.next;
		}

		// If key was not present in linked list
		if (temp == null)
			return list;

		// Unlink the node from linked list
		prev.next = temp.next;

		return list;
	}

	public static LinkList createLinkedList() {

		LinkList L = new LinkList();

		L.insertFirst(60);
		L.insertFirst(50);
		L.insertFirst(40);
		L.insertFirst(30);
		L.insertFirst(20);
		L.insertFirst(10);

		L.displayList(L.head);

		return L;
	}

}
