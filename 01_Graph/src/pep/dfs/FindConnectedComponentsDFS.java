package pep.dfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FindConnectedComponentsDFS {
	/**
7
5
0 1 10
2 3 10
4 5 10
5 6 10
4 6 10
	 
	 */
	

	/**
	 
	 0           3             4
	 |           |			   | \
	 |			 |			   |  \
	 |			 |			   |	\
	 .           .             .	  \		
	 1           2			   5 ----- 6	
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
		int vertices = Integer.parseInt(br.readLine());
		
		// initializing graph with empty list
		ArrayList<Edge>[] graph = new ArrayList[vertices];
		for(int i=0;i< vertices;i++){
			graph[i] = new ArrayList<>();
		}
		
		boolean visited[] = new boolean[vertices];

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

		List<List<Integer>> comps = new ArrayList();
		// for each unvisited node call the method and add list of connected components to mail list
		
		for(int i=0;i<vertices;i++){
			if(visited[i] == false){
				List<Integer> comp = new ArrayList();
				getConnectedComponent(graph, i, visited, comp);
				comps.add(comp);
			}
		}
		
		System.out.println(comps);
		
		
	}
	
	// practice and see in how much less time you can finish this
	public static void getConnectedComponent(ArrayList<Edge>[] graph, int src, boolean visited[], List<Integer> comp){
		/**
		 * Steps
		 1. mark the node visited
		 2. add node to list
		 3. what should be the terminating condition
		 		 
		 */
		
		visited[src] = true;
		comp.add(src);
		ArrayList<Edge> edges = graph[src];
		for(Edge edge : edges){		
			if(!visited[edge.nbr]){ // this is the terminating condition i think, when all nodes gets visited
				getConnectedComponent(graph, edge.nbr, visited, comp);		
			}
		}
		
	}


}
