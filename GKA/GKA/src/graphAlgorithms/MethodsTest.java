package graphAlgorithms;

import static org.junit.Assert.*;
import static graphReader.main.*;
import static graphAlgorithms.*;

import java.util.*;

import graph.Graph;




import org.junit.Test;

public class MethodsTest {

	@Test
	public void testBellmanFord() {
		//Test für einen gerichteten Graphen
		Graph graph = readGraphGewicht("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_03.graph");
		List<Integer> vertexes = graph.getVertexes();
		int s = vertexes.get(0); int u = vertexes.get(1);
		int x = vertexes.get(2); int v = vertexes.get(3);
		int y = vertexes.get(4);
		Methods.bellmanFord(graph, s);
		assertEquals(0,graph.getValV(s, "distanz"));
		assertEquals(8,graph.getValV(u, "distanz"));
		assertEquals(5,graph.getValV(x, "distanz"));
		assertEquals(9,graph.getValV(v, "distanz"));
		assertEquals(7,graph.getValV(y, "distanz"));
		assertEquals(-1,graph.getValV(s, "vorgaenger"));
		assertEquals(x,graph.getValV(u, "vorgaenger"));
		assertEquals(s,graph.getValV(x, "vorgaenger"));
		assertEquals(u,graph.getValV(v, "vorgaenger"));
		assertEquals(x,graph.getValV(y, "vorgaenger"));
		
		//Test für einen ungerichteten Graphen
		graph = readGraphGewicht("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_10.graph");
		vertexes = graph.getVertexes();
		int v1 = vertexes.get(0); int v2 = vertexes.get(1);
		int v3 = vertexes.get(2); int v4 = vertexes.get(3);
		int v5 = vertexes.get(4);
		Methods.bellmanFord(graph, v3);
		assertEquals(10,graph.getValV(v1,"distanz"));
		assertEquals(15,graph.getValV(v2,"distanz"));
		assertEquals(0,graph.getValV(v3, "distanz"));
		assertEquals(25,graph.getValV(v4,"distanz"));
		assertEquals(30,graph.getValV(v5,"distanz"));
		assertEquals(v3,graph.getValV(v1, "vorgaenger"));
		assertEquals(v1,graph.getValV(v2, "vorgaenger"));
		assertEquals(-1,graph.getValV(v3, "vorgaenger"));
		assertEquals(v1,graph.getValV(v4, "vorgaenger"));
		assertEquals(v1,graph.getValV(v5, "vorgaenger"));
	}

	@Test
	public void testFloydWarshall() {
		//Test für einen gerichteten Graphen
		Graph graph = readGraphGewicht("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_03.graph");
		HashMap<String,List<List<Integer>>> hash = Methods.floydWarshall(graph);
		List<Integer> listDist = Arrays.asList(0,8,5,9,7);
		List<Integer> listTrans = Arrays.asList(-1,3,-1,3,3);
		assertEquals(listDist, hash.get("dist").get(0));
		assertEquals(listTrans,hash.get("trans").get(0));
		
		//Test für einen ungerichteten Graphen
		graph = readGraphGewicht("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_10.graph");
		hash = Methods.floydWarshall(graph);
		listDist = Arrays.asList(10,15,0,25,30);
		assertEquals(listDist, hash.get("dist").get(2));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testFloydWarshallExc() {
		Graph graph = readGraphGewicht("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_07.graph");
		HashMap<String,List<List<Integer>>> hash = Methods.floydWarshall(graph);
	}

}
