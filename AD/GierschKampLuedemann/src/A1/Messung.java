package A1;

import java.util.*;


/** Klasse zum Speichern und Auswerten von Messreihen von Zahlen
 * 
 * @author Steffen Giersch, Birger Kamp, Maria L�demann */
public class Messung {

	/** Liste aller Messungen */
	private List<Double> mssg = new ArrayList<Double>();

	/** Summe aller Messungen f�r den Mittelwert */
	public double akku_sum = 0.0;
	
	/** Getter f�r die Liste der Messungen
	 * @return Liste der Messungen
	 */
	public List<Double> getMessungen() {
		return mssg;
	}

	/** Constructor f�r die Messung */
	public Messung() {}

	/** F�gt einen Messwert hinzu
	 * 
	 * @param n Hinzuzuf�gender Messwert */
	public void add(double n) {
		mssg.add(n);											// n der Liste aller Messungen hinzuf�gen
		akku_sum = akku_sum + n; 								// Summe aller Messwerte um n erh�hen
	}

	/** Errechnet den Mittelwert aller Messungen
	 * 
	 * @return Mittelwert aller Messungen */
	public double average() {
		if (mssg.size() == 0) {
			return 0;
		} 														// Division durch 0 verhindern
		else {
			return (akku_sum / mssg.size());
		} 														// Durchschnitt ausrechnen und zur�ckgeben
	}

	/** Errechnet die Varianz der Messungen
	 * 
	 * @return Varianz der Messungen */
	public double varianz() {
		if (mssg.size() <= 1) {
			return 0;
		} 														// Division durch 0 oder -1 verhindern
		else {
			double akku = 0;
			for (Double d : mssg) {								// Varianz ausrechnen und zur�ckgeben
				akku = akku + (Math.pow(d - average(), 2.0));	// akku = akku + (d-average)^2
			}
			return Math.sqrt(akku / (mssg.size() - 1));			// Aus dem Ergebnis die Wurzel ziehen um die Varianz zu erhalten und zur�ckgeben
		}
	}
	public String toString(){
		System.out.println(mssg.size());
		return mssg.toString();
	}
	public void printStringforExcel(){
		for(double val : mssg){
			System.out.println((int)val);
		}
	}
}
