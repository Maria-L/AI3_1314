package A1;

import java.util.*;


/** Klasse zum Speichern und Auswerten von Messreihen von Zahlen
 * 
 * @author Steffen Giersch, Birger Kamp, Maria Lüdemann */
public class Messung {

	/** Liste aller Messungen */
	private List<Double> mssg = new ArrayList<Double>();

	/** Summe aller Messungen für den Mittelwert */
	public double akku_avg = 0.0;
	
	/** Getter für die Liste der Messungen
	 * @return Liste der Messungen
	 */
	public List<Double> getMessungen() {
		return mssg;
	}

	/** Constructor für die Messung */
	public Messung() {}

	/** Fügt einen Messwert hinzu
	 * 
	 * @param n Hinzuzufügender Messwert */
	public void add(double n) {
		mssg.add(n);											// n der Liste aller Messungen hinzufügen
		akku_avg = akku_avg + n; 								// Summe aller Messwerte um n erhöhen
	}

	/** Errechnet den Mittelwert aller Messungen
	 * 
	 * @return Mittelwert aller Messungen */
	public double average() {
		if (mssg.size() == 0) {
			return 0;
		} 														// Division durch 0 verhindern
		else {
			return (akku_avg / mssg.size());
		} 														// Durchschnitt ausrechnen und zurückgeben
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
			for (Double d : mssg) {								// Varianz ausrechnen und zurückgeben
				akku = akku + (Math.pow(d - average(), 2.0));	// akku = akku + (d-average)^2
			}
			return Math.sqrt(akku / (mssg.size() - 1));			// Aus dem Ergebnis die Wurzel ziehen um die Varianz zu erhalten und zurückgeben
		}
	}
}
