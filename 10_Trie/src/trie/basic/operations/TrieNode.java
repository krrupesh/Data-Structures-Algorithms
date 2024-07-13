package trie.basic.operations;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

	public Map<Character,TrieNode> map;
	boolean endofWord;
	
	public TrieNode() {
		map = new HashMap<Character,TrieNode>();
	}

	@Override
	public String toString() {
		return ""+map+ "EW->"+endofWord;//"N [M=" + map + ", EW=" + endofWord + "]"
	}	
}
