package graph;


public class Id { 
	private int id_max; 
	
	public Id(int id){ 
		this.id_max = id-1; 
	} 
	
	public int newID(){ 
		id_max = id_max +1; 
		return id_max; 
	} 
	
	public int errorID() {
		return -1;
	}
}