package PR3;


public interface IHotelsystemFassade {
	/**Einlogvorgang des Benutzers. Wirft Exception wenn falsch.
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschlüsselt) des Kunden
	 */
	void login(String name, String passwort);
	/**Sucht für die Auswahloberfläche eine Liste an Zimmern die in dem gewählten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende);
	/**Sucht für die Auswahloberfläche eine Liste an Zimmern des passenden Typs die in dem gewählten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gewählten Zimmers
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp);
	/**Sucht für die Auswahloberfläche eine Liste an Zimmern des passenden Typs und genügend Platz die in dem gewählten Zeitraum nicht belegt sind.
	 * @param start Start des Belegungszeitraums
	 * @param ende Ende des Belegungszeitraums
	 * @param zimmertyp Typnummer des gewählten Zimmers
	 * @param anzahlPersonen Anzahl der unterzubringenden Personen
	 * @return Liste der passenden Zimmer
	 */
	List<Integer> findeFreieZimmerMit(DatumTyp start, DatumTyp ende, int zimmertyp, int anzahlPersonen);
	/**Blockiert Zimmer im System um zu verhindern, dass Zimmer öfter reserviert werden können.
	 * @param zimmernummer ist die Zimmernummer und gleichzeitig die ID des Zimmers
	 */
	void lockZimmer(int zimmernummer);
	/**Fügt die Zusatzdienstleistungen zur Reservierung zusammen
	 * 
	 * @param leistungen eine Liste an Zusatzdienstleistung Objekten die aus der Oberfläche übernommen werden
	 */
	void addZugitsatzdienstleistungen(List<Zusatzdienstleistung> leistungen);
	void newReservierung(int kundenID, DatumTyp start, DatumTyp ende, Hash<Integer, Integer> zimmernummerZuPersonenzahlMap, List<Zusatzleistung> zusatzleistungen, String wuensche);
	
}
