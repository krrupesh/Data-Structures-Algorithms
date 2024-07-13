package trie.applications;

import java.util.Map;

import trie.basic.operations.TrieDataStructures;
import trie.basic.operations.TrieNode;

public class DictionarySearch {

	public static void main(String[] args) {

		TrieDataStructures tds = new TrieDataStructures();
		
		tds.insertTrie("abc");
		System.out.println(tds);

		//tds.insertTrie("abclm");
		//tds.insertTrie("albc");
		tds.insertTrie("abcdefgh");
		System.out.println(tds);

		tds.insertTrie("abcdegh");
		System.out.println(tds);

		//tds.insertTrie("abcdekgh");
		//System.out.println(tds);


		/*tds.insertTrie("abkc");
		tds.insertTrie("anbc");
		tds.insertTrie("abcopm");
		tds.insertTrie("abcxyz");*/
		
		DictionarySearch ds = new DictionarySearch();
		ds.displayMatchingWords("abc", tds);
	}

	public void displayMatchingWords(String str,TrieDataStructures tds){
		
		Map<Character, TrieNode> map = tds.root.map;
		TrieNode lastNode = null;
		for (Character c : str.toCharArray()) {
			lastNode = map.get(c);
			map = lastNode.map;
		}
		
		for(Map.Entry<Character, TrieNode> e : map.entrySet()){
			char c = e.getKey();
			StringBuilder str1 = new StringBuilder(str);
			getMap(str1,c,map);
			System.out.println(str1);
			
		}
	}

	private void getMap(StringBuilder str, char c, Map<Character, TrieNode> map) {

		str = str.append(c);
		Map<Character, TrieNode> map1 = map.get(c).map;
		
		if(map1.size()>0){
			findWords(map1,str);
		}
	}

	private void findWords(Map<Character, TrieNode> map1, StringBuilder str) {

		for(Map.Entry<Character, TrieNode> e : map1.entrySet()){
			char c = e.getKey();
			getMap(str,c,map1);
			System.out.println(str);
		}
	}
}
