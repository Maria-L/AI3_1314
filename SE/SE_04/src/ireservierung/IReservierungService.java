package ireservierung;

import reservierung.*;

public interface IReservierungService {

	/**
	 * Eine Funktion die eine Zusatzdienstleistung erzeugt und in die Datenbank speichert
	 * 
	 * @param name Der Name der gewünschten Dienstleistung
	 * @return liefert das erzeugte Zusatzdienstleistungs Objekt
	 */
	public Zusatzleistung erzeugeZusatzleistung(String name);
	
	/**
	 * Reserviert ein Zimmer wobei ein Reservierungsobjekt erzeugt und in die Datenbank gespeichert wird
	 * 
	 * @param gastNr Die ID des Gastes der reserviert
	 * @param zimmerNr Die Nummer des Zimmers das reserviert werden soll
	 * @return Das soeben erzeugte Reservierungsobjekt
	 */
	public Reservierung reserviereZimmer(int gastNr, int zimmerNr);
	/**
	 * Bucht eine Zusatzdienstleistung zu einer Reservierung
	 * 
	 * @param reservierungsNr die Nummer der Reservierung auf der gebucht werden soll
	 * @param zusatzleistungNr die Nummer der Dienstleistung die gebucht werden soll
	 */
	public void bucheZusatzleistung(int reservierungsNr, int zusatzleistungNr);
}
