package PR3;

import java.util.*;


public interface IHotelsystemFassade {
	/**Kommando:
	 * Muss vor jeder anderen Funktion aufgerufen und erfolgreich beendet werden.
	 * Einlogvorgang des Benutzers.  wenn falsch.
	 * @pre passwort ist angemessen verschl�sselt
	 * @param name Name des Kunuden != null
	 * @param passwort Passwort (verschl�sselt) des Kunden
	 * @throws IllegalArgumentException Exception f�r den Fall, dass das Passwort nicht zum Namen passt falsch eingegeben wurde
	 */
	void login(String name, String passwort) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende
	 * @param start Start des Belegungszeitraums != null
	 * @param ende Ende des Belegungszeitraums != null
	 * @return Liste der passenden Zimmer. Wenn keine passenden Zimmer gefunden wurde wird die leere Liste zur�ck gegeben
	 * @throws IllegalArgumentException Wenn start >= ende
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 
	 * @param start Start des Belegungszeitraums != null
	 * @param ende Ende des Belegungszeitraums != null
	 * @param zimmertyp Typnummer des gew�hlten Zimmers != null
	 * @return Liste der passenden Zimmer. Wenn keine passenden Zimmer gefunden wurde wird die leere Liste zur�ck gegeben
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs und gen�gend Platz die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen 
	 * @param start Start des Belegungszeitraums != null
	 * @param ende Ende des Belegungszeitraums != null
	 * @param zimmertyp Typnummer des gew�hlten Zimmers != null
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen != null
	 * @return Liste der passenden Zimmer. Wenn keine passenden Zimmer gefunden wurde wird die leere Liste zur�ck gegeben
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde oder anzahlPersonen < 1
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen) throws IllegalArgumentException;
	
	/**Abfrage:
	 * Kann jederzeit durchgef�hrt werden.
	 * L�sst sich von einem Zimmer Informationen geben
	 * @param zimmernummer ID des Zimmers != null
	 * @return List<Integer> in der Form [AnzahlBetten, ZimmerArt, Grundpreis]
	 * @throws IllegalArgumentException Wenn eine nicht existierende Zimmernummer gew�hlt wurde
	 */
	List<Integer> getZimmerInformationen(Integer zimmernummer) throws IllegalArgumentException;
	
	/**Kommando:
	 * Kann nach dem einloggen jederzeit, allerdings maximal ein mal pro Zimmer pro Tag pro Benutzer ausgef�hrt werden
	 * Blockiert Zimmer im System um zu verhindern, dass Zimmer �fter reserviert werden k�nnen.
	 * Diese Blockierung wird automatisch nach 10 Minuten aufgehoben.
	 * @pre zimmernummer geh�rt zu einem existierenden Zimmer das noch nicht belegt ist
	 * @param zimmernummer Die Zimmernummer und gleichzeitig die ID des Zimmers != null
	 * @throws IllegalArgumentException Wenn das Zimmer schon belegt oder geloggt ist oder nicht existiert 
	 */
	void lockZimmer(int zimmernummer) throws IllegalArgumentException;

	/**Kommando:
	 * Kann nach dem einloggen jederzeit aufgef�hrt werden
	 * Erstellt die fertige Reservierung. Diese ist nun fix und eine Rechnung kann verschickt werden.
	 * @pre start < ende und 0 < zimmertyp < 4 und 0 < anzahlPersonen  und 0 < |zimmernummerZuPersonenzahlMap|
	 * @param kundenID ID des Kunden != null
	 * @param start Start des Belegungszeitraumes != null
	 * @param ende Ende des Belegungszeitraumes != null
	 * @param zimmernummerZuPersonenzahlMap Map die von Zimmernummer auf die Anzahl der Personen in diesem Zimmer mapt != null
	 * @param zusatzleistungen Liste der hinzugebuchten Zusatzdienstleistungen, leere Liste wenn keine gew�hlt
	 * @param wuensche Spezielle W�nsche des Kunden als einfacher String, leerer String wenn keine W�nsche
	 * @return Ist die Reservierung erfolgreich gewesen (true) oder nicht (false)
	 */
	boolean newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Integer> zusatzleistungen, String wuensche);
	
}
