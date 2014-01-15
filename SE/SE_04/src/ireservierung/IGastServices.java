package ireservierung;
import reservierung.*;

import reservierung.Gast;

public interface IGastServices {
	
	/**
	 * Erzeugt ein Gast Objekt und speichert es in die Datenbank
	 * 
	 * @param nr die ID unter der der Gast gespeichert werden soll
	 * @param name Name des Gastes
	 * @param email verifizierter Email Adressen Typ, zu speichende Email Adresse
	 * @return Ein Gast Objekt
	 */
	public Gast erzeugeGast(int nr, String name,EmailTyp email);
	
	/**
	 * Sucht in der Datenbank nach einem Gast Objekt mit dem gesuchten Namen
	 * 
	 * @param name der Name nach dem in der Datenbank gesucht werden soll
	 * @return Ein Gast Objekt
	 */
	public Gast sucheGastNachName(String name);
}
