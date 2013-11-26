package A6;

public class Produkt {
	private int vol;
	private int price;
	private String name;
	
	public Produkt(String name, int vol, int price) {
		this.vol = vol;
		this.price = price;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getVol() {
		return vol;
	}
	
	public String toString() {
		return "Name: " + name + "\tVolumen: " + vol + "\tPreis: " + price;
	}
}
