package p.reverselinklist.recursion;

public class LinkListApp {

	public static void main(String[] args) {

		LinkList L1 = new LinkList();
		L1.insertFirst(10);
		L1.insertFirst(20);
		L1.insertFirst(30);
		L1.insertFirst(40);
		L1.insertFirst(50);
		L1.insertFirst(60);
		//L1.insertFirst(70);		
		
		L1.displayLinkList();
		
		/*L1.reverseWithoutRecursion();
		
		System.out.println();
				
		L1.displayLinkList();
		
		L1.reverseUsingRecursion();
		
		System.out.println();
		
		L1.displayLinkList();*/
		
		//L1.reverse(L1.head);
		
		System.out.println();
		
		//L1.displayLinkList();
		
		L1.printListFromBothSides(L1.head);
		


	}

}
