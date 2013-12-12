package PR3;

import java.util.*;


public interface IZimmerkomponenteServices {
	
	/**Abfrage:
	 * Kann jederzeit aufgerufen werden
	 * Gibt zu dem eingegebenen Zimmer die dazu passende Art des Zimmers zurück
	 * @param zimmer ZimmerID des abzufragenden Zimmers
	 * @return ID der Zimmerart
	 * @throws IllegalArgumentException Wenn das Zimmer nicht existiert
	 */
	Integer getZimmerArt(Integer zimmer) throws IllegalArgumentException;
}
