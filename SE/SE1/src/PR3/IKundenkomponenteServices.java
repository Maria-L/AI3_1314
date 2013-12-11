package PR3;


public interface IKundenkomponenteServices {
	/**Kommando:
	 * Muss vor jeder anderen Funktion aufgerufen und erfolgreich beendet werden.
	 * Einlogvorgang des Benutzers.  wenn falsch.
	 * @pre passwort ist angemessen verschlüsselt
	 * @param name Name des Kunuden
	 * @param passwort Passwort (verschlüsselt) des Kunden
	 * @throws IllegalArgumentException Exception für den Fall, dass das Passwort falsch eingegeben wurde
	 */
	void login(String name, String passwort) throws IllegalArgumentException;
}
