package reservierung;

public class Zusatzleistung {

	private int nr;
	private String leistungsArt;
	
	public Zusatzleistung(int num, String lArt) {
		nr = num;
		leistungsArt = lArt;
		
	}

	public int getNr(){
		return nr;
	}
	
	public String getLeistungsArt(){
		return leistungsArt;
	}
}
