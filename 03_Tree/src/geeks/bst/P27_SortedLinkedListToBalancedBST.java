package geeks.bst;

public class P27_SortedLinkedListToBalancedBST {

	int count = 0;
	
	public static void main(String[] args) {
		P27_SortedLinkedListToBalancedBST llist = new P27_SortedLinkedListToBalancedBST();
        
        /* Let us create a sorted linked list to test the functions
           Created linked list will be 7->6->5->4->3->2->1 */
        llist.push(80);
		llist.push(70);
        llist.push(60);
        llist.push(50);
        llist.push(40);
        llist.push(30);
        llist.push(20);
 
        System.out.println("Given Linked List ");
        llist.printList(head);

        /* Convert List to BST */
        TNode root = llist.sortedListToBST();
        System.out.println("");
        System.out.println("In-Order Traversal of constructed BST ");
        llist.inOrder(root);
        
        System.out.println("\npre-Order Traversal of constructed BST ");
        llist.preOrder(root);
	}

    
    /* This function counts the number of nodes in Linked List
    and then calls sortedListToBSTRecur() to construct BST */
	private TNode sortedListToBST() {
	     /*Count the number of nodes in Linked List */
		 System.out.println("head : "+head.data);
	     int n = countNodes(head);
	
	     /* Construct BST */
	     return sortedListToBSTRecur(n);
	 }
	 
	  /* The main function that constructs balanced BST and returns root of it.
      n  --> No. of nodes in the Doubly Linked List */
	  private TNode sortedListToBSTRecur(int n) {
		  count++;
		  
		  if(head != null){			  
			  System.out.println("count : "+count+" head : "+head.data+" n : "+n);
		  }
	      /* Base Case */
	      if (n <= 0) 
	          return null;
	
	      /* Recursively construct the left subtree */
	      TNode left = sortedListToBSTRecur(n / 2);
	
	      /* head_ref now refers to middle node, make middle node as root of BST*/
	      TNode root = new TNode(head.data);
	      System.out.println("root : "+root.data);
	
	      // Set pointer to left subtree
	      root.left = left;
	
	      /* Change head pointer of Linked List for parent recursive calls */
	      head = head.next;
	      System.out.println("head : "+head.data);

	
	      /* Recursively construct the right subtree and link it with root. The number of nodes in right subtree  is 
	         total nodes - nodes in left subtree - 1 (for root) */
	      root.right = sortedListToBSTRecur(n - n / 2 - 1);
	
	      return root;
	  }
  
  
	  private TNode sortedListToBSTRecur1(int n) {

	      if (n <= 0) 
	          return null;
	
	      TNode left = sortedListToBSTRecur(n / 2);
	
	      TNode root = new TNode(head.data);	
	      root.left = left;	
	      head = head.next;

	      root.right = sortedListToBSTRecur(n - n / 2 - 1);
	
	   return root;
	  }
  

	  /* UTILITY FUNCTIONS */
	  /* A utility function that returns count of nodes in a given Linked List */
	  int countNodes(LNode head) {
	      int count = 0;
	      LNode temp = head;
	      while (temp != null)
	      {
	          temp = temp.next;
	          count++;
	      }
	      return count;
	  }
  
    /* Function to insert a node at the beginging of the Doubly Linked List */
	void push(int new_data){
	   /* allocate node */
	   LNode new_node = new LNode(new_data);
	
	   /* since we are adding at the begining, prev is always NULL */
	   new_node.prev = null;
	
	   /* link the old list off the new node */
	   new_node.next = head;
	
	   /* change prev of head node to new node */
	   if (head != null)
	       head.prev = new_node;
	
	   /* move the head to point to the new node */
	   head = new_node;
	}

	/* Function to print nodes in a given linked list */
	void printList(LNode node) {
	   while (node != null) 
	   {
	       System.out.print(node.data + " ");
	       node = node.next;
	   }
	}
	
    /* A utility function to print inorder traversal of BST */
    void inOrder(TNode node) {
        if (node == null)
            return;
        inOrder(node.left);
        System.out.print(node.data + " ");
        inOrder(node.right);
    }
    
    /* A utility function to print preorder traversal of BST */
    void preOrder(TNode node) {
        if (node == null)
            return;
        System.out.print(node.data + " ");
        preOrder(node.left);
        preOrder(node.right);
    }
	
    /* head node of link list */
    static LNode head;
     
    /* Link list Node */
    class LNode {
        int data;
        LNode next, prev;
 
        LNode(int d) {
            data = d;
            next = prev = null;
        }
    }
    
    /* A Binary Tree Node */
    class TNode {
        int data;
        TNode left, right;
 
        TNode(int d) {
            data = d;
            left = right = null;
        }
    }
    
}
