package PR3;

import java.util.*;


public interface IHotelsystemFassade {
	/**Kommando:
	 * Muss vor jeder anderen Funktion aufgerufen und erfolgreich beendet werden.
	 * Einlogvorgang des Benutzers.  wenn falsch.
	 * @pre passwort ist angemessen verschl�sselt
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschl�sselt) des Kunden
	 * @throws IllegalArgumentException Exception f�r den Fall, dass das Passwort falsch eingegeben wurde
	 */
	void login(String name, String passwort) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs und gen�gend Platz die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen 
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde oder anzahlPersonen < 1
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen) throws IllegalArgumentException;
	
	/**Kommando:
	 * Kann nach dem einloggen jederzeit, allerdings maximal ein mal pro Zimmer pro Tag ausgef�hrt werden
	 * Blockiert Zimmer im System um zu verhindern, dass Zimmer �fter reserviert werden k�nnen.
	 * @pre zimmernummer geh�rt zu einem existierenden Zimmer das noch nicht belegt ist
	 * @param zimmernummer Die Zimmernummer und gleichzeitig die ID des Zimmers
	 * @throws IllegalArgumentException Wenn das Zimmer schon belegt oder geloggt ist oder nicht existiert 
	 */
	void lockZimmer(int zimmernummer) throws IllegalArgumentException;

	/**Kommando:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Erstellt die fertige Reservierung. Diese ist nun fix und eine Rechnung kann verschickt werden.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen  und 0 < |zimmernummerZuPersonenzahlMap|
	 * @param kundenID ID des Kunden
	 * @param start Start des Belegungszeitraumes
	 * @param ende Ende des Belegungszeitraumes
	 * @param zimmernummerZuPersonenzahlMap Map die von Zimmernummer auf die Anzahl der Personen in diesem Zimmer mapt
	 * @param zusatzleistungen Liste der hinzugebuchten Zusatzdienstleistungen
	 * @param wuensche Spezielle W�nsche des Kunden als einfacher String
	 * @return Ist die Reservierung erfolgreich gewesen (true) oder nicht (false)
	 */
	boolean newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Zusatzleistung> zusatzleistungen, String wuensche);
	
}
