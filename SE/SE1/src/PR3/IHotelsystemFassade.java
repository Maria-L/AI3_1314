package PR3;


public interface IHotelsystemFassade {
	/**Einlogvorgang des Benutzers. Wirft Exception wenn falsch.
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschl�sselt) des Kunden
	 */
	void login(String name, String passwort);
	/**Sucht f�r die Auswahloberfl�che eine Liste an Zimmern die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende);
	/**Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp);
	/**Sucht f�r die Auswahloberfl�che eine Liste an Zimmern des passenden Typs und gen�gend Platz die in dem gew�hlten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gew�hlten Zimmers
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen);
	/**Blockiert Zimmer im System um zu verhindern, dass Zimmer �fter reserviert werden k�nnen.
	 * @param zimmernummer ist die Zimmernummer und gleichzeitig die ID des Zimmers
	 */
	void lockZimmer(int zimmernummer);
	/**F�gt die Zusatzdienstleistungen zur Reservierung zusammen
	 * 
	 * @param leistungen eine Liste an Zusatzdienstleistung Objekten die aus der Oberfl�che �bernommen werden
	 */
	void addZugitsatzdienstleistungen(List<Zusatzdienstleistung> leistungen);
	void newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Zusatzleistung> zusatzleistungen, String wuensche);
	
}
