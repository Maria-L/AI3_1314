package PR3;

import java.util.*;


public interface IHotelsystemFassade {
	/**Kommando:
	 * Muss vor jeder anderen Funktion aufgerufen und erfolgreich beendet werden.
	 * Einlogvorgang des Benutzers.  wenn falsch.
	 * @pre passwort ist angemessen verschlüsselt
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschlüsselt) des Kunden
	 * @throws IllegalArgumentException Exception für den Fall, dass das Passwort falsch eingegeben wurde
	 */
	void login(String name, String passwort) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgeführt werden
	 * Sucht für die Auswahloberfläche eine Liste an Zimmern die in dem gewählten Zeitraum nicht belegt sind.
	 * @pre start < ende
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgeführt werden
	 * Sucht für die Auswahloberfläche eine Liste an Zimmern des passenden Typs die in dem gewählten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gewählten Zimmers
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gewählt wurde
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgeführt werden
	 * Sucht für die Auswahloberfläche eine Liste an Zimmern des passenden Typs und genügend Platz die in dem gewählten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen 
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gewählten Zimmers
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gewählt wurde oder anzahlPersonen < 1
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen) throws IllegalArgumentException;
	
	/**Kommando:
	 * Kann nach dem einloggen jederzeit, allerdings maximal ein mal pro Zimmer pro Tag ausgeführt werden
	 * Blockiert Zimmer im System um zu verhindern, dass Zimmer öfter reserviert werden können.
	 * @pre zimmernummer gehört zu einem existierenden Zimmer das noch nicht belegt ist
	 * @param zimmernummer Die Zimmernummer und gleichzeitig die ID des Zimmers
	 * @throws IllegalArgumentException Wenn das Zimmer schon belegt oder geloggt ist oder nicht existiert 
	 */
	void lockZimmer(int zimmernummer) throws IllegalArgumentException;

	/**Kommando:
	 * Kann nach dem einloggen jederzeit aufgeführt werden
	 * Erstellt die fertige Reservierung. Diese ist nun fix und eine Rechnung kann verschickt werden.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen  und 0 < |zimmernummerZuPersonenzahlMap|
	 * @param kundenID ID des Kunden
	 * @param start Start des Belegungszeitraumes
	 * @param ende Ende des Belegungszeitraumes
	 * @param zimmernummerZuPersonenzahlMap Map die von Zimmernummer auf die Anzahl der Personen in diesem Zimmer mapt
	 * @param zusatzleistungen Liste der hinzugebuchten Zusatzdienstleistungen
	 * @param wuensche Spezielle Wünsche des Kunden als einfacher String
	 * @return Ist die Reservierung erfolgreich gewesen (true) oder nicht (false)
	 */
	boolean newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Zusatzleistung> zusatzleistungen, String wuensche);
	
}
