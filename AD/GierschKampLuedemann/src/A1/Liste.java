package A1;

/** Implementation einer einfach verketteten Liste
 * @author Steffen Giersch, Birger Kamp, Maria L�demann
 */
public interface Liste {

	/** Ein Element vorne anf�gen
	 * 
	 * @param x Zuzuf�gendes Element */
	public void cons(Object x);

	/** Das vorderste Element von der Liste entfernen und zur�ckgeben
	 * 
	 * @return Erstes Element der Liste oder bei leerer Liste eine
	 *         NullPointerException */

	public Object head();

	/** Das vorderste Element der Liste ausgeben (ohne entfernen)
	 * 
	 * @return Erstes Element der Liste */
	public Object top();

	/** Gibt die L�nge der Liste aus
	 * 
	 * @return L�nge der Liste */
	public int length();

	/** Ist die Liste leer oder nicht
	 * 
	 * @return True (leer)oder False (nicht leer) */
	public boolean isempty();

	/** Nach n Elementen x in die Liste einf�gen
	 * 
	 * @param x Einzuf�gendes Element
	 * @param n Position in der Liste, an der das Element eingef�gt werden soll
	 *            - beginnend mit 0
	 * @return Wurde das Element erfolgreich eingef�gt (True) oder nicht (False) */
	public boolean insert(Object x, int n);

	/**
	 * Gibt die Anzahl an Dereferenzierungen aus
	 * @return Anzahl an Dereferenzierungen
	 */
	public int getStepCounter();
	
	/**
	 * Setzt den Dereferenzierungscounter zur�ck
	 */
	public void resetStepCounter();
}
