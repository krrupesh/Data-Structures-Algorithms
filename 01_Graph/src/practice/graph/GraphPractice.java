package practice.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import dfs.Graph;
import dfs.StackX;
import dfs.Vertex;


public class GraphPractice {
	
	private final int MAX_VERTEX = 10;
	private char vertexList[];
	private int adjMatx[][];
	private int vertexCount;
	private Stack<Integer> vertexStack;
	private Queue<Integer> vertexQueue;

	private boolean [] visited;
	
	
	public static void main(String[] args) {

		GraphPractice graph = new GraphPractice();

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


		//graph.dfs();
		
		graph.bfs();

			
	}

	public GraphPractice() {

		vertexList = new char[MAX_VERTEX];
		adjMatx = new int[MAX_VERTEX][MAX_VERTEX];
		visited = new boolean[MAX_VERTEX];
		vertexCount = 0;

		// initializing all elements of adjacency matrix with zero
		for (int j = 0; j < MAX_VERTEX; j++) {
			for (int k = 0; k < MAX_VERTEX; k++) {
				adjMatx[j][k] = 0;
			}
		}

		vertexStack = new Stack();	
		vertexQueue= new LinkedList<>();
	}
	
	// Depth first search using stack.
	public void dfs(){
		
		vertexStack.push(0);
		visited[0] = true;
		System.out.println(vertexList[0]);
		
		while(!vertexStack.isEmpty()){
			
			int vertexIndex = vertexStack.peek();
			
			// check if this vertex has any unvisited adjacent vertex.
			int unvisitedVertex = checkAdjUnvisitedVertex(vertexIndex); 
			
			// if yes then
			// 1. push vertex to stack
			// 2. mark vertex visited
			// 3. print that vertex
			if(unvisitedVertex != -1){
				vertexStack.push(unvisitedVertex);
				visited[unvisitedVertex] = true;
				System.out.println(vertexList[unvisitedVertex]);
			}else{
				// pop vertex from stack
				vertexStack.pop();
			}									
		}			
	}
	
	public void bfs(){
		
		vertexQueue.offer(0);
		visited[0] = true;
		System.out.println(vertexList[0]);
		
		while(!vertexQueue.isEmpty()){
			
			int vertexIndex = vertexQueue.poll();

			for(int i=0;i<MAX_VERTEX;i++){
				if(adjMatx[vertexIndex][i] == 1 && !visited[i]){
					vertexQueue.offer(i);
					visited[i] = true;
					System.out.println(vertexList[i]);
				}
			}
			
		}
			
	}


	private int checkAdjUnvisitedVertex(int vertexIndex) {

		for(int i=0;i<MAX_VERTEX;i++){
			if(adjMatx[vertexIndex][i] == 1 && !visited[i]){
				return i;
			}
		}
		
		return -1;
	}
	
	
	public void addVertex(char lab) {
		vertexList[vertexCount++] = lab;
	}

	public void addEdge(int start, int end) {
		adjMatx[start][end] = 1;
		adjMatx[end][start] = 1;
	}
}
