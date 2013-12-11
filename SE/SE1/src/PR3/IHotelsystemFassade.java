package PR3;


public interface IHotelsystemFassade {
	/**Kommando:
	 * Einlogvorgang des Benutzers.  wenn falsch.
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschl�sselt) des Kunden
	 * @throws IllegalArgumentException Exception f�r den Fall, dass das Passwort falsch eingegeben wurde
	 */
	void login(String name, String passwort) throws IllegalArgumentException;
	/**Abfrage:
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende) throws IllegalArgumentException;
	/**Abfrage:
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp) throws IllegalArgumentException;
	/**Abfrage:
	 * Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs und gen�gend Platz die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen
	 * @return Liste der passenden Zimmer
	 * @throws IllegalArgumentException Wenn start >= ende oder ein nicht existenter Zimmertyp gew�hlt wurde oder anzahlPersonen < 1
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen) throws IllegalArgumentException;
	/**Kommando:
	 * Blockiert Zimmer im System um zu verhindern, dass Zimmer �fter reserviert werden k�nnen.
	 * @param zimmernummer Die Zimmernummer und gleichzeitig die ID des Zimmers
	 * @throws IllegalArgumentException Wenn das Zimmer schon belegt oder geloggt ist oder nicht existiert 
	 */
	void lockZimmer(int zimmernummer) throws IllegalArgumentException;
	/**Kommando:
	 * F�gt die Zusatzdienstleistungen zur Reservierung zusammen
	 * @param leistungen eine Liste an Zusatzdienstleistung Objekten die aus der Oberfl�che �bernommen werden
	 */
	void addZugitsatzdienstleistungen(List<Zusatzdienstleistung> leistungen);
	/**Kommando:
	 * Erstellt die fertige Reservierung. Diese ist nun fix und eine Rechnung kann verschickt werden.
	 * @param kundenID ID des Kunden
	 * @param start Start des Belegungszeitraumes
	 * @param ende Ende des Belegungszeitraumes
	 * @param zimmernummerZuPersonenzahlMap Map die von Zimmernummer auf die Anzahl der Personen in diesem Zimmer mapt
	 * @param zusatzleistungen Liste der hinzugebuchten Zusatzdienstleistungen
	 * @param wuensche Spezielle W�nsche des Kunden als einfacher String
	 */
	void newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Zusatzleistung> zusatzleistungen, String wuensche);
	
}
