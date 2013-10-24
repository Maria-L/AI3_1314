package graph;

import graphAlgorithms.Methods;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GraphImpl start = new GraphImpl();
		int k1,k2,k3;
		int e1,e2,e3;
		
		k1 = start.addVertex("Knoten1");
		k2 = start.addVertex("Knoten2");
		k3 = start.addVertex("Knoten3");
		
		e1 = start.addEdgeU(k1,k2);
		e2 = start.addEdgeU(k2,k3);
		e3 = start.addEdgeU(k3,k1);
		
		start.setValE(e1, "gewicht", 1);
		start.setValE(e2, "gewicht", 1);
		start.setValE(e3, "gewicht", 3);

		Methods.bellmanFord(start, k1);
		System.out.println(start.getValV(k1, "distanz"));
		System.out.println(start.getValV(k2, "distanz"));
		System.out.println(start.getValV(k3, "distanz"));
		
		System.out.println(start.getValV(k3, "vorgaenger"));
	}

}
