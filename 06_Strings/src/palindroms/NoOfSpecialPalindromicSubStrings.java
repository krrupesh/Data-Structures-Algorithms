package palindroms;

import java.util.ArrayList;
import java.util.Iterator;

public class NoOfSpecialPalindromicSubStrings {

	public static void main(String[] args) {

		String str = "ccacacabccacabaaaabbcbccbabcbbcaccabaababcbcacabcabacbbbccccabcbcabbaaaaabacbcbbbcababaabcbbaaababababbabcaabcaacacbbaccbbabbcbbcbacbacabaaaaccacbaabccabbacabaabaaaabbccbaaaabacabcacbbabbacbcbccbbbaaabaaacaabacccaacbcccaacbbcaabcbbccbccacbbcbcaaabbaababacccbacacbcbcbbccaacbbacbcbaaaacaccbcaaacbbcbbabaaacbaccaccbbabbcccbcbcbcbcabbccbacccbacabcaacbcaccabbacbbccccaabbacccaacbbbacbccbcaaaaaabaacaaabccbbcccaacbacbccaaacaacaaaacbbaaccacbcbaaaccaabcbccacaaccccacaacbcacccbcababcabacaabbcacccbacbbaaaccabbabaaccabbcbbcaabbcabaacabacbcabbaaabccabcacbcbabcbccbabcabbbcbacaaacaabbbabbaacbbacaccccabbabcbcabababbcbaaacbaacbacacbabbcacccbccbbbcbcabcabbbcaabbaccccabaabbcbcccabaacccccaaacbbbcbcacacbabaccccbcbabacaaaabcccaaccacbcbbcccaacccbbcaaaccccaabacabcabbccaababbcabccbcaccccbaaabbbcbabaccacaabcabcbacaccbaccbbaabccbbbccaccabccbabbbccbaabcaabcabcbbabccbaaccabaacbbaaaabcbcabaacacbcaabbaaabaaccacbaacababcbacbaacacccacaacbacbbaacbcbbbabccbababcbcccbccbcacccbababbcacaaaaacbabcabcacaccabaabcaaaacacbcc";

		System.out.println(noofPalindromeSubstrings(str));

		/*
		 * 
		 * a----------- b----------- c----------- bcb abcba b-----------
		 * a----------- bab b----------- aba a----------- 11
		 */

	}

	public static int noofPalindromeSubstrings(String str) {

		char ch[] = str.toCharArray();
		int totalcount = 0;

		int count = 0;
		boolean endReached = false;
		boolean elseEntered = false;

		for (int i = 0; i < ch.length - 1; i++) {
			if (ch[i] == ch[i + 1]) {
				System.out.println(ch.length + "  ,i " + i);

				if (i == ch.length - 2) {
					endReached = true;
				}

				count++;
				System.out.println("count " + count);

			} else {
				elseEntered = true;

				if (count > 0) {
					totalcount = totalcount + ((count + 1) * (count + 2)) / 2;
					count = 0;
				}

				if (i < ch.length - 1) {
					totalcount = totalcount + noofPalindromeSubstringsWithCenterAti(i, ch);

					if (i == ch.length - 2) {
						totalcount = totalcount + 1;
					}

				}
			}
		}

		if (count > 0) {
			totalcount = totalcount + ((count + 1) * (count + 2)) / 2;
			count = 0;
		}
		/*
		 * if(endReached){ if(count > 0){ totalcount = totalcount + ((count + 1)
		 * * (count + 2)) / 2; count = 0; } }
		 * 
		 * if(!elseEntered){ totalcount = totalcount + ((count + 1) * (count +
		 * 2)) / 2; }
		 */

		return totalcount;
	}

	public static int noofPalindromeSubstringsWithCenterAti(int i, char arr[]) {
		int count = 1;
		System.out.println(arr[i] + "-----------");

		if (i == 0 || i == arr.length - 1) {
			return 1;
		}

		for (int j = i - 1, k = i + 1; j >= 0 && k < arr.length; j--, k++) {

			if (count == 1) {
				if (arr[j] == arr[k]) {
					count++;
					printsubstring(j, k, arr);
				} else {
					break;
				}
			} else {
				if (arr[j] == arr[k] && (arr[j + 1] == arr[j])) {
					count++;

					if ((arr[j + 1] == arr[j])) {
						count++;
					}

					printsubstring(j, k, arr);
				} else {
					break;
				}
			}

		}
		return count;

	}

	public static void printsubstring(int start, int end, char arr[]) {
		for (int i = start; i <= end; i++) {
			System.out.print(arr[i]);
		}
		System.out.println();
	}

	// -Working--------------------------------------------------------------------------------------

	// Complete the substrCount function below.
	static long substrCount(int n, String s) {

		return substrCount1(n, s);
	}

	static class Point {
		public char key;
		public long count;

		public Point(char x, long y) {
			key = x;
			count = y;
		}
	}

	// Complete the substrCount function below.
	static long substrCount1(int n, String s) {
		s = s + " ";
		ArrayList<Point> l = new ArrayList<Point>();
		long count = 1;
		char ch = s.charAt(0);
		for (int i = 1; i <= n; i++) {
			if (ch == s.charAt(i))
				count++;
			else {
				l.add(new Point(ch, count));
				count = 1;
				ch = s.charAt(i);
			}
		}
		count = 0;
		if (l.size() >= 3) {
			Iterator<Point> itr = l.iterator();
			Point prev, curr, next;
			curr = (Point) itr.next();
			next = (Point) itr.next();
			count = (curr.count * (curr.count + 1)) / 2;
			for (int i = 1; i < l.size() - 1; i++) {
				prev = curr;
				curr = next;
				next = itr.next();
				count += (curr.count * (curr.count + 1)) / 2;
				if (prev.key == next.key && curr.count == 1)
					count += prev.count > next.count ? next.count : prev.count;
			}
			count += (next.count * (next.count + 1)) / 2;
		} else {
			for (Point curr : l) {
				count += (curr.count * (curr.count + 1)) / 2;
			}
		}
		return count;
	}

}
