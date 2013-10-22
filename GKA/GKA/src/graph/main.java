package graph;


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
		e3 = start.addEdgeD(k3,k1);
		
//		start.deleteEdge(k2,k1);
//		start.deleteEdge(k3,k1);
		start.deleteVertex(k1);
		start.deleteVertex(k3);
		
		System.out.println(start.getAdjacent(k2));
		System.out.println(start.getIncident(k2));
		
//		start.getSource(e2);
//		start.getTarget(e2);
		
		start.getVertexes();
		start.getEdges();
		
		System.out.println("Success!");

		
	}

}
