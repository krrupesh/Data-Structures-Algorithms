import java.io.*;
import java.util.*;

class Solution {

  static char[] reverseWords(char[] arr) {
    int n = arr.length;
    
    List<List<Character>> list = new ArrayList<>();
    List<Character> temp = new ArrayList<>();
    
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == ' ') {
        list.add(new ArrayList<>(temp));
        temp = new ArrayList<>();
      }
      temp.add(arr[i]);
    }
    
    System.out.println(list);
    
    int words = list.size();
    char[] ans = new char[n];
    int index = 0;
    
    for (int i = words - 1; i >= 0; i--) {
      List<Character> word = list.get(i);
      for (Character ch : word) {
        ans[index] = ch;
        index++;
      }
      if (index != n - 1) {
        ans[index] = ' ';
        index++;
      }
    }
    
    return ans;
  }

  public static void main(String[] args) {
  }

}

/*
  [ 'p', 'e', 'r', 'f', 'e', 'c', 't', '  ',
    'm', 'a', 'k', 'e', 's', '  ',
    'p', 'r', 'a', 'c', 't', 'i', 'c', 'e' ]
    
    list = [['p', 'e', 'r', 'f', 'e', 'c', 't'], ]
    temp = []
    
*/