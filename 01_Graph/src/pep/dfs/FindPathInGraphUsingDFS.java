package pep.dfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FindPathInGraphUsingDFS {
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
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		ArrayList<Edge>[] graph = createGraph();
		boolean visited[] = new boolean[graph.length];

		// 1. find path from source to destination
		boolean pathFound = findPath(graph, 0, 6, visited);
		System.out.println("pathFound : "+pathFound);
		
		
		// 2. find all paths
		printAllPath(graph, 0, 6, visited, 0+"");
		
		
	}

	/**	 
	 0 --------- 3------------ 4
	 |           |			   | \
	 |			 |			   |  \
	 |			 |			   |	\
	 .           .             .	  \		
	 1 --------- 2			   5 ----- 6	
	 */
	
	
	public static boolean findPath(ArrayList<Edge>[] graph, int src, 
			int dest, boolean visited[]){
		/**
		 * Steps
		 1. get nbr's from src
		 2. see if dest is equal to src return true
		 3. how to mark nodes visited		 		 
		 */
		
		// Step-1 , checking if source is equal to destination
		if(src == dest){
			return true;
		}
		
		// Step-2, marking the node visited
		visited[src] = true;
		
		// Step-3, getting nbr's of the node 
		ArrayList<Edge> edges = graph[src];
		
		for(Edge edge : edges){		
			// Step-4, check if that nbr is not already visited
			if(!visited[edge.nbr]){
				boolean hasPath = findPath(graph, edge.nbr, dest, visited);		
				if(hasPath){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public static void printAllPath(ArrayList<Edge>[] graph, int src, int dest, boolean visited[], String pfs){
		/**
		 * Steps
		 1. get nbr's from src
		 2. see if dest is equal to src return true
		 3. how to mark visited nodes
		 		 
		 */
		if(src == dest){
			// here print the path
			System.out.println(pfs);
			return;
		}
		visited[src] = true;
		ArrayList<Edge> edges = graph[src];
		for(Edge edge : edges){		
			if(!visited[edge.nbr]){ // 
				printAllPath(graph, edge.nbr, dest, visited, pfs+" "+edge.nbr);
				// mark node unvisited while returning back, need its visualization
				// visited[src] = false;
			}
		}
		// why we are marking it unvisited
		visited[src] = false;

		
		return;
	}

	
	public static ArrayList<Edge>[] createGraph(){
		
		int vertices = 7;
		// initializing graph with empty list
		ArrayList<Edge>[] graph = new ArrayList[vertices];
		for(int i=0;i< vertices;i++){
			graph[i] = new ArrayList<>();
		}
		
		graph[0].add(new Edge(0,1,10));
		graph[0].add(new Edge(0,3,10));
		
		graph[1].add(new Edge(1,0,10));
		graph[1].add(new Edge(1,2,10));
		
		graph[2].add(new Edge(2,1,10));
		graph[2].add(new Edge(2,3,10));
		
		graph[3].add(new Edge(3,0,10));
		graph[3].add(new Edge(3,2,10));
		graph[3].add(new Edge(3,4,10));
		
		graph[4].add(new Edge(4,3,10));
		graph[4].add(new Edge(4,5,10));
		graph[4].add(new Edge(4,6,10));
		
		graph[5].add(new Edge(5,4,10));
		graph[5].add(new Edge(5,6,10));
		
		graph[6].add(new Edge(6,4,10));
		graph[6].add(new Edge(6,5,10));
		
		return graph;
		
	}
	
	
	public static ArrayList<Edge>[] createGraphDynamically() throws NumberFormatException, IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
		int vertices = Integer.parseInt(br.readLine());
		
		// initializing graph with empty list
		ArrayList<Edge>[] graph = new ArrayList[vertices];
		for(int i=0;i< vertices;i++){
			graph[i] = new ArrayList<>();
		}
		
		// adding edges to arraylist
		int edges = Integer.parseInt(br.readLine());		
		for(int i=0;i<edges;i++){
			String input[] = br.readLine().split(" ");
			
			int v1 = Integer.parseInt(input[0]);
			int v2 = Integer.parseInt(input[1]);
			int wt = Integer.parseInt(input[2]);
			
			// add bi-directional edges
			graph[v1].add(new Edge(v1, v2, wt)); 
			graph[v2].add(new Edge(v2, v1, wt));
		}// now we got a graph
		
		
		return graph;
	}
}
