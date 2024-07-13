package trie.basic.operations;

import java.util.Map;

public class TrieDataStructures {

	public TrieNode root;

	public TrieDataStructures() {
		root = new TrieNode();
	}

	public void insertTrie(String str) {
		Map<Character, TrieNode> map = root.map;
		TrieNode lastNode = null;

		for (Character c : str.toCharArray()) {

			if (!map.containsKey(c)) {
				map.put(c, new TrieNode());
			}

			lastNode = map.get(c);
			map = lastNode.map;
		}

		lastNode.endofWord = true;
	}

	public boolean searchTrie(String str) {

		Map<Character, TrieNode> map = root.map;
		TrieNode lastNode = null;
		
		for (Character c : str.toCharArray()) {

			if (!map.containsKey(c)) {
				return false;
			}
			lastNode = map.get(c);
			map = lastNode.map;

		}
		if (lastNode.endofWord) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "TDS [root=" + root + "]";
	}
}
