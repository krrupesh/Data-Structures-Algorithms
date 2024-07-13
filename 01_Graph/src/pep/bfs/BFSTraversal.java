package pep.bfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;



public class BFSTraversal {
	/**
7
8
0 1 10
0 3 10
1 2 10
2 3 10
3 4 10
4 5 10
5 6 10
4 6 10
	 
	 */
	

	// Steps
	// 1. create graph
	// 2. find path
	
	/**
	 
	 0 --------- 3------------ 4
	 |           |			   | \
	 |			 |			   |  \
	 |			 |			   |	\
	 .           .             .	  \		
	 1 --------- 2			   5 ----- 6	
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
		int vertices = Integer.parseInt(br.readLine());
		
		// Step-1 , initializing graph with empty list
		// index of graph represents each vertex 
		ArrayList<Edge>[] graph = new ArrayList[vertices];
		for(int i=0;i< vertices;i++){
			// ArrayList for each node containing its nbr's (edges(v1,v2,wt1) here )
			graph[i] = new ArrayList<>();
		}
		// empty graph created so far
		
		// Step-2 , creating boolean array
		boolean visited[] = new boolean[vertices];

		// Step-3, get no of edges
		int edges = Integer.parseInt(br.readLine());
		
		
		for(int i=0;i<edges;i++){
			String input[] = br.readLine().split(" ");
			
			int v1 = Integer.parseInt(input[0]);
			int v2 = Integer.parseInt(input[1]);
			int wt = Integer.parseInt(input[2]);
			
			// Step-4,  add bi-directional edges to each array list of edges
			graph[v1].add(new Edge(v1, v2, wt));// arraylist containing edges emerging from vertex v1
			graph[v2].add(new Edge(v2, v1, wt));// arraylist containing edges emerging from vertex v2
		}// now we got a graph
		// there can be many edges emerging from a vertex
				
		// find path from source to destination
		printBFSTraversal(graph, 0,  visited);		
	}
	
	public static void printBFSTraversal(ArrayList<Edge>[] graph, int src, 
			boolean visited[]){
		/**
		 * Steps
		 1. pop an element from queue
		 2. get all the nbr's and add it to the queue
		 3. mark it visited
		 4. continue this process until queue is empty		 		 
		 */
		Queue<Integer> queue = new LinkedList<>();
		// Step-1, add source node to queue and mark it visited
		queue.add(src);		
		visited[src] = true;
		
		while(!queue.isEmpty()){
			// Step-2, poll from queue
			int vertex = queue.poll();
			System.out.print(vertex+" ");
			
			// Step-3, get all the edges for that vertex
			ArrayList<Edge> edges = graph[vertex];
			
			// Step-4, iterate through all the nbr's for the node
			for(Edge edge : edges){
				// Step-5, check if node is not visited
				if(!visited[edge.nbr]){
					// Step-6, check if node is not visited
					queue.add(edge.nbr);
					// Step-7, same node can be nb'r for many nodes so we need to mark
					// it visited once we add it to queue
					visited[edge.nbr] = true;	
				}				
			}
		}
	}// end
	
	
	public static void printBFS(ArrayList<Edge> [] graph, int src, boolean visited[]){
		
		Queue<Integer> queue = new LinkedList<>();
		queue.add(src);
		visited[src] = true;
		
		while(!queue.isEmpty()){
			int vertex = queue.poll();
			System.out.print(vertex+" ");
			ArrayList<Edge> edges = graph[vertex];
			for(Edge edge : edges){
				if(!visited[edge.nbr]){
					queue.add(edge.nbr);
					visited[edge.nbr] = true;
				}			
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
