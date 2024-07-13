package oct05.geeks.graph;

import java.util.LinkedList;

public class Graph {

	private int V;
	LinkedList<Integer> adjencyList[];
	
	public Graph(int v) {
		V = v;
		adjencyList = new LinkedList[V];
		for(int i=0;i<v;i++){
			System.out.println("initializing list at "+i);
			adjencyList[i] = new LinkedList<>();	
		}	
	}

	public static void main(String[] args) {
		
		// create graph adjacency list or matrix
		Graph g = new Graph(4); 
		  
        g.addEdge(0, 1); 
        g.addEdge(0, 2); 
        g.addEdge(1, 2); 
        g.addEdge(2, 0); 
        g.addEdge(2, 3); 
        g.addEdge(3, 3);
				
		// traverse using DFS
        g.DFS(2);
	}

	private void DFS(int v){
		boolean visited[] = new boolean[V];
		
		DFSUtil(2, visited);
	}
	
	
	/**
	 * 
	 * get the list of vertices
	 * iterate through it
	 * 
	 */
	private void DFSUtil(int v, boolean visited[]){
		
		visited[v] = true;
		System.out.println("visited vertex : "+v);

		for(Integer i : adjencyList[v]){
			
			if(!visited[i]){
				DFSUtil(i, visited);
			}			
		}		
	}
	
	private void addEdge(int v, int w) {
		adjencyList[v].add(w);		
	}

}
