package A1;

/** Implementation einer einfach verketteten Liste
 * @author Steffen Giersch, Birger Kamp, Maria Lüdemann
 */
public interface Liste {

	/** Ein Element vorne anfügen
	 * 
	 * @param x Zuzufügendes Element */
	public void cons(Object x);

	/** Das vorderste Element von der Liste entfernen und zurückgeben
	 * 
	 * @return Erstes Element der Liste oder bei leerer Liste eine
	 *         NullPointerException */

	public Object head();

	/** Das vorderste Element der Liste ausgeben (ohne entfernen)
	 * 
	 * @return Erstes Element der Liste */
	public Object top();

	/** Gibt die Länge der Liste aus
	 * 
	 * @return Länge der Liste */
	public int length();

	/** Ist die Liste leer oder nicht
	 * 
	 * @return True (leer)oder False (nicht leer) */
	public boolean isempty();

	/** Nach n Elementen x in die Liste einfügen
	 * 
	 * @param x Einzufügendes Element
	 * @param n Position in der Liste, an der das Element eingefügt werden soll
	 *            - beginnend mit 0
	 * @return Wurde das Element erfolgreich eingefügt (True) oder nicht (False) */
	public boolean insert(Object x, int n);

	/**
	 * Gibt die Anzahl an Dereferenzierungen aus
	 * @return Anzahl an Dereferenzierungen
	 */
	public int getStepCounter();
	
	/**
	 * Setzt den Dereferenzierungscounter zurück
	 */
	public void resetStepCounter();
}
