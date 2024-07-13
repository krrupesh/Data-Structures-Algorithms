package tree.traversals;
import java.io.*;
import java.util.*;

/***********************************************************
 * CODE INSTRUCTIONS:                                      *
 * 1) The method findLargestSmallerKey you're              *
 *    asked to implement is located at line 36.            *
 * 2) Use the helper code below to implement it.           *
 * 3) In a nutshell, the helper code allows you to         *
 *    to build a Binary Search Tree.                       *
 * 4) Jump to line 82 to see an example for how the        *
 *    helper code is used to test findLargestSmallerKey.   *
 ***********************************************************/


class FindNoSmallerThanNum {
 
  static class Node {
    int key;
    Node left;
    Node right; 
    Node parent;
    
    Node(int key) {
      this.key = key;
      left = null;
      right = null;
      parent = null;
    }
  }
  
  static class BinarySearchTree {
    
    Node root;
    
    int findLargestSmallerKey(int num) {
      // your code goes here 
      List<Integer> list = new ArrayList();
/*      
      inOrderTraversal( root,  num, list);
      System.out.println(list);
      
      for(int i=list.size()-1;i>=0;i--){
        if(list.get(i) < num){
          return list.get(i);
        }
      }
*/      
      
      Boolean found = false;
      int result = inOrderTraversalOptimized( root,  num, found);
      
      if(found){
        return result;
      }
      
      return -1;
    }
    
    void inOrderTraversal(Node current, int num, List<Integer> list){
      
      if(current == null){
        return;
      }
      
      inOrderTraversal(current.left, num, list);
      list.add(current.key);
      inOrderTraversal(current.right, num, list);
    }
    
    
      int inOrderTraversalOptimized(Node current, int num, Boolean found){
      
        if(current == null){
          return Integer.MAX_VALUE;
        }

        inOrderTraversalOptimized(current.right, num, found);
        if(current.key < num){
          found = true;
          return current.key;
        }
        return inOrderTraversalOptimized(current.left, num, found);
    }
    
    //  inserts a new node with the given number in the
    //  correct place in the tree
    void insert(int key) {
      
      // 1) If the tree is empty, create the root
      if(this.root == null) {
        this.root = new Node(key);
        return;
      }
      
      // 2) Otherwise, create a node with the key
      //    and traverse down the tree to find where to
      //    to insert the new node
      Node currentNode = this.root;
      Node newNode = new Node(key); 
      
      while(currentNode != null) {
        if(key < currentNode.key) {
          if(currentNode.left == null) {
            currentNode.left = newNode;
            newNode.parent = currentNode;
            break;
          } else {
            currentNode = currentNode.left;
          }
        } else {
          if(currentNode.right == null) {
            currentNode.right = newNode;
            newNode.parent = currentNode;
            break;
          } else {
            currentNode = currentNode.right;
          }
        }
      }
    }
  }

  /*********************************************
   * Driver program to test above function     *
   *********************************************/ 
   
   public static void main(String[] args) {
     
     // Create a Binary Search Tree
     BinarySearchTree bst = new BinarySearchTree();
     bst.insert(20);
     bst.insert(9);
     bst.insert(25);
     bst.insert(5);
     bst.insert(12);
     bst.insert(11);
     bst.insert(14);
     
     int result = bst.findLargestSmallerKey(17);
     System.out.println("Largest smaller number is " + result);
    
  }
}