package ireservierung;

public abstract class Entity_ID {

	int idStand;
	
	public int getID(){
		idStand ++;
		return idStand;
	}
	
	public void refresh(){
		idStand = 0;
	}
	
	private int idStand(){
		return idStand;
	}

}
