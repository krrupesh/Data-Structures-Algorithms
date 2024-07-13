package p40.swap.adjacent.element;

import basic.singly.linkedlist.Node;
import basic.singly.linkedlist.LinkList;
import basic.singly.linkedlist.LinkListApp;

public class SwapAdjacentElements {

	public static void main(String[] args) {
		
		LinkList L = LinkListApp.createLinkedList();
		
		swapLinkedList(L);
		
		L.displayList();
	}
	
	public static LinkList swapLinkedList(LinkList L){
		
		Node current = L.head;
		
		while(current.next != null){
			System.out.println(current.data);
			
			Node temp = current.next.next;// 30
			current.next.next = current; // 10
			current = temp; // 30
			
			if(current == null){
				return L;
			}
		}
		
		
		return L;
	}

}
