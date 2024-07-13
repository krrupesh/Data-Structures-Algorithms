package test;

import java.util.*;

public class MyStringChain {

	static Map<String,Integer> wordCount = new HashMap<String, Integer>();
	static List <String> wordsList = new ArrayList<String>();
	
	public static void main(String[] args) {

		String []words = {"a","b","ba","bca","bda","bdca"};
		
		findLength(words);
	}

	public static void findLength(String [] words){
				
		for(int i=0;i<words.length;i++){
			wordsList.add(words[i]);
		}
		
		int length = 0;
		wordCount.put(wordsList.get(0), 1);		
		length = wordsList.get(0).length();
		
		for(int i=1;i<wordsList.size();i++){

			String word = wordsList.get(i);
			if(word.length() == length){
				wordCount.put(word, 1);
			}else{
				removeSingleChar(word);
			}
		}
	}

	private static void removeSingleChar(String word) {

		int maxCount = 0;
		for(int i=0;i<word.length();i++){
			String wordR = word.replace(""+word.charAt(i),"");
			if(wordsList.contains(wordR)){
				int count = wordCount.get(wordR);
				if(count > maxCount){
					maxCount = count;
				}
			}
		}
		
		wordCount.put(word, maxCount + 1);
		
		System.out.println(wordCount);
	}

}
