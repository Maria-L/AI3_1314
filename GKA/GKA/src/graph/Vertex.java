package graph;

import java.util.*;


public class Vertex {
	private int id;
	private HashMap<String, Integer> intMap = new HashMap<String,Integer>();;
	private HashMap<String, String> stringMap = new HashMap<String,String>();;
	
	private List<Integer> incidentList = new ArrayList<Integer>();
	private List<Integer> adjacentList = new ArrayList<Integer>();
	
	/** Konstruktor von Vertex
	 * @param name Name (String) des neuen Knotens
	 * @param id ID (int) des neuen Knotens
	 */
	Vertex(String name, int id) {
		this.id = id;
		stringMap.put("name", name);
	}
	
	public int getID() {
		return id;
	}
	
	public void addAdjacent(int vid){
		adjacentList.add(vid);
	}
	
	public void addIncident(int eid){
		incidentList.add(eid);
	}
	
	public void deleteAdjacent(int vid){
		adjacentList.removeAll(Arrays.asList(vid));
	}
	
	public void deleteIncident(int eid){
		incidentList.removeAll(Arrays.asList(eid));
	}
	
	public List<Integer> getAdjacentList() {
		return adjacentList;
	}
	
	public List<Integer> getIncidentList() {
		return incidentList;
	}
	
	public HashMap<String, Integer> getIntMap(){
		return intMap;
	}
	
	public HashMap<String, String> getStringMap(){
		return stringMap;
	}
	
}
