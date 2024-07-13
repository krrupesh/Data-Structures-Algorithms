package practice.graph;

import java.util.LinkedList;
import java.util.Queue;

import dfs.StackX;
import dfs.Vertex;

public class GraphShortestPath {

	private final int MAX_VETX = 20;
	private Vertex vertexList[];
	private int adjMatx[][];
	private int vertexCount;
	private StackX theStack;

	private Queue<Integer> theQueue;
	
	public static void main(String[] args) {

		GraphShortestPath graph = new GraphShortestPath();

		graph.addVertex('A');
		graph.addVertex('B');
		graph.addVertex('C');
		graph.addVertex('D');
		graph.addVertex('E');
		graph.addVertex('F');
		graph.addVertex('G');
		graph.addVertex('H');

		graph.addEdge(0, 1);
		
		graph.addEdge(1, 2);
		//graph.addEdge(1, 4);
		graph.addEdge(1, 7);

		graph.addEdge(2, 3);
		graph.addEdge(2, 4);

		graph.addEdge(4, 5);
		graph.addEdge(4, 6);
		graph.addEdge(4, 7);

		graph.findShortestPath();
		
	}

	public GraphShortestPath() {

		vertexList = new Vertex[MAX_VETX];
		adjMatx = new int[MAX_VETX][MAX_VETX];
		vertexCount = 0;

		// initializing all elements of adjacency matrix with zero
		for (int j = 0; j < MAX_VETX; j++) {
			for (int k = 0; k < MAX_VETX; k++) {
				adjMatx[j][k] = 0;
			}
		}

		theStack = new StackX();
		
		theQueue = new LinkedList<Integer>();
	}

	public void addVertex(char lab) {
		vertexList[vertexCount++] = new Vertex(lab);
	}

	public void addEdge(int start, int end) {
		adjMatx[start][end] = 1;
		adjMatx[end][start] = 1;
	}

	public void diaplayVertex(int v) {
		System.out.println(vertexList[v].label);
	}

	/*
	 * here v is index of vertex in linkedlist
	 */
	public int getAdjUnvisitedVertex(int v) {

		for (int j = 0; j < vertexCount; j++) {
			if (adjMatx[v][j] == 1 && vertexList[j].wasVisited == false) {
				return j;
			}
		}
		return -1;
	}
	
	public void findShortestPath() {

		vertexList[0].wasVisited = true;
		vertexList[0].level = 0;
		displayVertex(0);
		theQueue.offer(0);
		int v2;

		while (!theQueue.isEmpty()) {

			int v1 = theQueue.remove();
			
			// for each vertex we first visit its all adjacent
			// nodes then move to next node
			while((v2 = getAdjUnvisitedVertex(v1)) != -1){
				vertexList[v2].wasVisited = true;
				theQueue.offer(v2);
				vertexList[v2].level = vertexList[v1].level + 1;
				displayVertex(v2);

			}
		}
			
		for (int j = 0; j < vertexCount; j++) {
			vertexList[j].wasVisited = false; // reset flags
		}
	}
	
	public void displayVertex(int v) {
		System.out.println("visited vertex : " + vertexList[v].label + ", level : "+vertexList[v].level);
	}
}
