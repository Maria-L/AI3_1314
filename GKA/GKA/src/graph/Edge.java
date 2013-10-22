package graph;

import java.util.HashMap;


public class Edge {
	
	private int id;
	private int source;
	private int target;
	private boolean directed;
	
	private HashMap<String, Integer> intMap = new HashMap<String,Integer>();
	private HashMap<String, String> stringMap = new HashMap<String,String>();

	/** Konstructor von Edge
	 * @param eid ID (int) der neuen Kante
	 * @param v1 Quellen ID (int) der neuen Kante
	 * @param v2 Senken ID (int) der neuen Kante
	 * @param directed_ Ist der Graph gerichtet (true) oder nicht (false)
	 */
	public Edge(int eid, int v1, int v2, boolean directed_) {
		id = eid;
		source = v1;
		target = v2;
		this.directed = directed_;
	}
	
	public int getID() {
		return id;
	}
	
	public boolean directed() {
		return directed;
	}
	
	public int getSource() {
		return source;
	}
	
	public int getTarget() {
		return target;
	}
	
	public HashMap<String, Integer> getIntMap(){
		return intMap;
	}
	
	public HashMap<String, String> getStringMap(){
		return stringMap;
	}
	
}
