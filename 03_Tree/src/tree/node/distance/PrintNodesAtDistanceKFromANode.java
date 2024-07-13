package tree.node.distance;

import java.util.ArrayList;
import java.util.List;

import basic.operations.BinarySearchTree;
import basic.operations.Node;

/* Let us create following BST
      50
   /     \
  30      70
 /  \    /  \
20   40  60   80 */

public class PrintNodesAtDistanceKFromANode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BinarySearchTree tree = new BinarySearchTree();
		tree.inorder();

		tree.insert(10);
		tree.insert(35);
		tree.insert(36);
		tree.insert(37);

		tree.insert(45);
		tree.insert(46);
		tree.insert(47);

		printNodesAtKDistance(tree.root, new Node(45), 3);

	}

	public static boolean getRootToNodePath(Node root, Node target, List<Node> list) {

		if (root == null) {
			return false;
		}

		if (root.key == target.key) {
			System.out.println("Target Adding : " + root.key);
			list.add(root);
			// mujhe apne parent node ko batana parega ki target node mil gya
			// hai,
			return true; // taaki wo apne uper wale ko bta sake
		}

		boolean finlc = getRootToNodePath(root.left, target, list);
		// mere left child ne true return kiya hai to mai add ho jata hun
		if (finlc) {
			System.out.println("Left Adding : " + root.key);
			list.add(root);
			return true;
		}

		boolean finrc = getRootToNodePath(root.right, target, list);
		// mere right child ne true return kiya hai to mai add ho jata hun
		if (finrc) {
			System.out.println("Right Adding : " + root.key);
			list.add(root);
			return true;
		}

		return false;
	}

	public static void printNodesAtKDistance(Node root, Node target, int k) {
		List<Node> list = new ArrayList<>();

		getRootToNodePath(root, target, list);

		System.out.println(list);

		// printing down nodes [easy]
		printDownNodes(list.get(0), null, k);

		// printing nodes which are up
		for (int i = 1; i < list.size(); i++) {

			// how to handle the case for first node in list
			printDownNodes(list.get(i), list.get(i - 1), k - i);

		}

	}

	public static void printDownNodes(Node node, Node ignore, int k) {

		if (node == null) {
			return;
		}

		if (ignore != null && node.key == ignore.key) {
			return;
		}

		if (k == 0) {
			System.out.print(node.key + " ");
		}

		printDownNodes(node.left, ignore, k - 1);

		printDownNodes(node.right, ignore, k - 1);

	}

}
