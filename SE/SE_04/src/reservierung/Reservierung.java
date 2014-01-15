package reservierung;

public class Reservierung {

	
	private int nr;
	private int zimmerNr;
	
	public Reservierung(int num, int zNr) {
		nr = num;
		zimmerNr = zNr;
	}
	
	public int getNr(){
		return nr;
	}
	
	public int getZimmerNr(){
		return zimmerNr;
	}

}
