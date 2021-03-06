package ireservierung;


public interface IGastServicesFuerReservierung {

	/**
	 * Eine Funktion, die den angegebenen Gast in der Datenbank auf Stammkunde setzt
	 * 
	 * @param nr Die Nummer des gewŁnschten Gastes
	 */
	public void markiereGastAlsStammkunden(int nr);
}
