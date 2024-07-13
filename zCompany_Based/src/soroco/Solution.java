package soroco;

import java.util.ArrayList;
import java.util.List;

public class Solution {

	public static void main(String[] args) {

		List<String> list = new ArrayList<String>();
		/*
		 * list.add("XOOXOOO"); list.add("XXOOOXO"); list.add("OXOOOXO");
		 */

		list.add("XOXOXX");
		list.add("XOOXOO");

		System.out.println(numberOfWalls(list));

	}

	static int numberOfWalls(List<String> board) {

		char ch[][] = new char[board.size()][board.get(0).length()];

		int i = 0;
		for (String str : board) {
			ch[i++] = str.toCharArray();
		}

		return numberOfIsland(ch);
	}

	public static int numberOfIsland(char[][] graph) {

		boolean[][] visited = new boolean[graph.length][graph[0].length];
		int count = 0;
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[i].length; j++) {
				if (visited[i][j] == false && graph[i][j] == 'X') {
					count++;
					DFS(graph, visited, i, j);
				}
			}
		}
		return count;
	}

	private static void DFS(char[][] graph, boolean[][] visited, int i, int j) {
		if (i < 0 || j < 0 || i == graph.length || j == graph[i].length) {
			return;

		}
		visited[i][j] = true;
		if (graph[i][j] == 'O') {
			return;
		}
		DFS(graph, visited, i, j + 1);
		DFS(graph, visited, i + 1, j);
		DFS(graph, visited, i + 1, j + 1);
		DFS(graph, visited, i - 1, j + 1);

	}

}
