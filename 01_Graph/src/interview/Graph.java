
package interview;

import java.io.*;
import java.util.*;

// This class represents a directed graph using adjacency list
// representation
class Graph
{
	private int V; // No. of vertices
	private LinkedList<Integer> adj[]; //Adjacency Lists

	// Constructor
	Graph()
	{
		
	}

	public void setV(int v){
		V = v;
		
		adj = new LinkedList[v];
		for (int i=0; i<v; ++i)
			adj[i] = new LinkedList();
	}
	
	// Function to add an edge into the graph
	void addEdge(int v,int w)
	{
		adj[v].add(w);
	}

	// prints BFS traversal from a given source s
	void BFS(int s, Map<Integer,String> mapIndex)
	{
		// Mark all the vertices as not visited(By default
		// set as false)
		boolean visited[] = new boolean[V];

		// Create a queue for BFS
		LinkedList<Integer> queue = new LinkedList<Integer>();

		// Mark the current node as visited and enqueue it
		visited[s]=true;
		queue.add(s);

		while (queue.size() != 0)
		{
			// Dequeue a vertex from queue and print it
			s = queue.poll();
			System.out.print(mapIndex.get(s));

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Iterator<Integer> i = adj[s].listIterator();
			while (i.hasNext())
			{
				int n = i.next();
				if (!visited[n])
				{
					visited[n] = true;
					queue.add(n);
				}
			}
		}
	}

	// Driver method to
	public static void main(String args[])
	{
		
		Scanner in = new Scanner(System.in);
		
		int noofpaths;
		
		noofpaths = in.nextInt();
		
		Map<String, Integer> map = new HashMap();
		
		Map<Integer,String> mapIndex = new HashMap();

		int index = 0;
		
		Graph g = new Graph();

		
		for(int i=0;i<=noofpaths;i++){
			
			String path = in.nextLine();
			String cities[] = path.split(" ");
			
			if(!map.containsKey(cities[0])){
				map.put(cities[0], index++);
				mapIndex.put(index-1, cities[0]);
			}
			
			System.out.println(cities[0]);
			System.out.println(cities[1]);

			
			if(!map.containsKey(cities[1])){
				map.put(cities[1], index++);
				mapIndex.put(index-1, cities[1]);

			}	
			
			g.addEdge(map.get(cities[0]), map.get(cities[1]));
		}
		
		
		g.setV(map.size());

		
		String first = in.next();
		

		g.BFS(map.get(first), mapIndex);
	}
}
// This code is contributed by Aakash Hasija
