package trie.basic.operations;

public class TrieRunner {

	public static void main(String[] args) {

		TrieDataStructures tds = new TrieDataStructures();
		
		tds.insertTrie("abc");
		System.out.println(tds);
		
		tds.insertTrie("abgl");
		System.out.println(tds);

		
		tds.insertTrie("cdf");
		System.out.println(tds);
		
		tds.insertTrie("abcd");
		System.out.println(tds);
		/*
		tds.insertTrie("lmn");	
		System.out.println(tds);*/
		
		//boolean found = tds.searchTrie("abcgl");
		//System.out.println("found : "+found);
	}

}
