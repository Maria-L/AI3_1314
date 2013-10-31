package graph;

import java.util.*;


public class GraphImpl implements Graph {

	/** ID-Objekt um neue, innerhalb dieses Graphen einzigartige IDs zu erzeugen */
	Id id;
	/** HashMap über alle Vertexes mit ihren IDs als Key */
	HashMap<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();
	/** HashMap über alle Edges mit ihren IDs als Key */
	HashMap<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
	
	boolean directed;

	public GraphImpl() {
		id = new Id(0);
	}
	
	@Override
	public boolean directed() {
		return directed;
	}
	
	@Override
	public void setDirected(boolean val) {
		directed = val;
	}

	@Override
	public int addVertex(String newItem) {
		int id_new = id.newID();
		Vertex vertex = new Vertex(newItem, id_new);
		vertexMap.put(id_new, vertex);
		return id_new;
	}

	@Override
	public void deleteVertex(int vid) {
		// 1.Durche jeden Knoten gehen und jeden Adjacenten == vid löschen
		// 2.Durch jede Kante gehen und diese Droppen, wenn Ziel oder Quelle == vid
		// 3.vid droppen
		HashSet<Vertex> vertexSet = new HashSet<Vertex>(vertexMap.values());
		for(Vertex v : vertexSet) {
			vertexMap.get(v.getID()).deleteAdjacent(vid);	// 1
		}
		
		HashSet<Edge> edgeSet = new HashSet<Edge>(edgeMap.values());
		for(Edge e : edgeSet) {
			if(e.getSource() == vid) {
				deleteEdge(vid, e.getTarget());
			} else if(e.getTarget() == vid) {	// 2
				deleteEdge(e.getSource(), vid);
			}
		}
		vertexMap.remove(vid);
	}

	@Override
	public int addEdgeU(int v1, int v2) {
		int id_new = id.newID();
		Edge edge = new Edge(id_new, v1, v2, false);
		vertexMap.get(v1).addIncident(id_new);
		vertexMap.get(v2).addIncident(id_new);
		vertexMap.get(v1).addAdjacent(v2);
		vertexMap.get(v2).addAdjacent(v1);
		edgeMap.put(id_new, edge);
		return id_new;
	}

	@Override
	public int addEdgeD(int v1, int v2) {
		int id_new = id.newID();
		Edge edge = new Edge(id_new, v1, v2, true);
		vertexMap.get(v1).addIncident(id_new);	//Es wir davon ausgegangen, dass zu v1 nur die neue Kante Inzident wird und 
		vertexMap.get(v1).addAdjacent(v2);		//zu v1 auch nur v2 adjazent wird, da diese nur für v1 benutzbar sind
		edgeMap.put(id_new, edge);
		return id_new;
	}

	@Override
	public void deleteEdge(int v1, int v2) {
		int edgeID = id.errorID();
		for (int i : vertexMap.get(v1).getIncidentList()) {
			if ((getTarget(i) == v2 && getSource(i) == v1)
					|| (getTarget(i) == v1 && getSource(i) == v2)) {
				edgeID = i;
			}
		}
		if (edgeID == id.errorID()) { throw new IllegalArgumentException(
				"Diese Kante existiert nicht!"); }
		if (edgeMap.get(edgeID).directed()) {
			vertexMap.get(v1).deleteIncident(edgeID);
			vertexMap.get(v1).deleteAdjacent(v2);
		} else {
			vertexMap.get(v1).deleteIncident(edgeID);
			vertexMap.get(v1).deleteAdjacent(v2);
			vertexMap.get(v2).deleteIncident(edgeID);
			vertexMap.get(v2).deleteAdjacent(v1);
		}

		edgeMap.remove(edgeID);
	}

	@Override
	public boolean isEmpty() {
		return edgeMap.keySet().isEmpty();
	}

	@Override
	public int getSource(int e1) {
		return edgeMap.get(e1).getSource();
	}

	@Override
	public int getTarget(int e1) {
		return edgeMap.get(e1).getTarget();
	}

	@Override
	public List<Integer> getIncident(int v1) {
		return vertexMap.get(v1).getIncidentList();
	}

	@Override
	public List<Integer> getAdjacent(int v1) {
		return vertexMap.get(v1).getAdjacentList();
	}

	@Override
	public List<Integer> getVertexes() {
		return new ArrayList<Integer>(vertexMap.keySet());
	}

	@Override
	public List<Integer> getEdges() {
		return new ArrayList<Integer>(edgeMap.keySet());
	}

	@Override
	public int getValE(int e1, String attr) {
		Integer akku = edgeMap.get(e1).getIntMap().get(attr);
		if(akku == null){
			return Integer.MAX_VALUE;
		}
		return akku;
	}

	@Override
	public int getValV(int v1, String attr) {
		Integer akku = vertexMap.get(v1).getIntMap().get(attr);
		if(akku == null){
			return Integer.MAX_VALUE;
		}
		return akku;
	}

	@Override
	public String getStrE(int e1, String attr) {
		String akku = edgeMap.get(e1).getStringMap().get(attr);
		if(akku == null){
			return "";
		}
		return akku;
	}

	@Override
	public String getStrV(int v1, String attr) {
		Vertex akku = vertexMap.get(v1);
		if(akku == null){
			return "";
		}
		return vertexMap.get(v1).getStringMap().get(attr);
	}

	@Override
	public List<String> getAttrV(int v1) {
		Set<String> akku = new HashSet<String>(vertexMap.get(v1).getIntMap().keySet());
		akku.addAll(vertexMap.get(v1).getStringMap().keySet());
		return new ArrayList<String>(akku);
	}

	@Override
	public List<String> getAttrE(int e1) {
		Set<String> akku = new HashSet<String>(edgeMap.get(e1).getIntMap().keySet());
		akku.addAll(edgeMap.get(e1).getStringMap().keySet());
		return new ArrayList<String>(akku);
	} 

	@Override
	public void setValE(int e1, String attr, int val) {
		edgeMap.get(e1).getIntMap().put(attr, val);

	}

	@Override
	public void setValV(int v1, String attr, int val) {
		vertexMap.get(v1).getIntMap().put(attr, val);
	}

	@Override
	public void setStrE(int e1, String attr, String val) {
		edgeMap.get(e1).getStringMap().put(attr, val);

	}

	@Override
	public void setStrV(int v1, String attr, String val) {
		vertexMap.get(v1).getStringMap().put(attr, val);
	}
	
	public void print() {
		System.out.println("\n\nKnoten:");
		for(int id : getVertexes()) {
			System.out.println("Name: " + getStrV(id, "name"));
			System.out.println("Distanz: " + getValV(id, "distanz"));
			System.out.println("Vorgänger: " + getStrV(getValV(id, "vorgaenger"),"name"));
		}
		
		System.out.println("\n\nKanten:\nQuelle: / Senke: / Gewicht");
		for(int id : getEdges()) {
			System.out.println(getStrV(getSource(id), "name") + " / " + getStrV(getTarget(id), "name") + " Gewicht: " + getValE(id, "gewicht"));
		}
		
	}

}
