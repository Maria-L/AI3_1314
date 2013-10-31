package graph;

import java.util.List;


public interface Graph {
	//Konstruktoren
	/**Fügt dem Graphen eine neue Ecke mit dem Namen newItem hinzu
	 * @param newItem Name der Ecke (name)
	 * @return ID (int) der Ecke
	 */
	int addVertex(String newItem);
	/** Löscht die Ecke mit der ID vid (int) aus dem Graphen und alle zu ihr inzidenten Kanten
	 * @param vid ID der Ecke
	 */
	void deleteVertex(int vid);
	/** Fügt dem Graphen eine neue ungerichtete Kante zwischen v1 (int) und v2 (int) hinzu
	 * @param v1 ID der ersten Ecke
	 * @param v2 ID der zweiten Ecke
	 * @return ID (int) der neuen Kante
	 */
	int addEdgeU(int v1, int v2);
	/** Fügt dem Graphen eine neue gerichtete Kante zwischen v1 (int) und v2 (int) hinzu
	 * @param v1 ID der Quelle
	 * @param v2 ID der Senke
	 * @return ID (int) der neuen Kante
	 */
	int addEdgeD(int v1, int v2);
	/** Löscht die Kante zwischen den beiden Ecken v1 (int) und v2 (int). Für den Fall von Mehrfachkanten wird eine gelöscht.
	 * @param v1 ID der ersten Ecke
	 * @param v2 ID der zweiten Ecke
	 */
	void deleteEdge(int v1, int v2);
	
	//Selektoren
	/** Prüft ob der Graph leer ist
	 * @return boolean, Graph ist leer -> true; Graph ist nicht leer -> false
	 */
	boolean isEmpty();
	/** Ermittelt im gerichteten Fall die Quelle der Kante e1 und im ungerichteten Fall die erste Ecke
	 * @param e1 ID (int) der Kante
	 * @return Gerichtete Kante -> Quelle (int); Ungerichtete Kante -> Erste Ecke (int)
	 */
	int getSource(int e1);
	/** Ermittelt im gerichteten Fall die Senke der Kante e1 und im ungerichteten Fall die zweite Ecke
	 * @param e1 ID (int) der Kante
	 * @return Gerichtete Kante -> Senke (int); Ungerichtete Kante -> Zweite Ecke (int)
	 */
	int getTarget(int e1);
	/** Ermittelt ale zur Ecke v1 (int) inzidenten Kanten
	 * @param v1 ID (int) der Ecke
	 * @return Liste der Kanten-IDs (List(Integer))
	 */
	List<Integer> getIncident(int v1);
	/** Ermittelt alle zur Ecke v1 (int) adjazenten Ecken
	 * @param v1 ID (int) der Ecke
	 * @return Liste der Ecken-IDs (List(Integer))
	 */
	List<Integer> getAdjacent(int v1);
	/** Ermittelt alle Ecken des Graphen
	 * @return Liste der Ecken des Graphen (List(Integer))
	 */
	List<Integer> getVertexes();
	/** Ermittelt alle Kanten des Graphen
	 * @return Liste der Kanten des Graphen (List(Integer))
	 */
	List<Integer> getEdges();
	/** Ermittelt den Attributwert von attr (String) der kante e1 (int) als Integer
	 * @param e1 ID (int) der Kante
	 * @param attr Attributname (String) 
	 * @return Wert (int) des Attributes | maxint im Fehlerfall
	 */
	int getValE(int e1, String attr);
	/** Ermittelt den Attributwert von attr (String) der Ecke v1 (int) als Integer
	 * @param v1 ID (int) der Ecke
	 * @param attr Attributname (String)
	 * @return Wert (int) des Attributes | maxint im Fehlerfall
	 */
	int getValV(int v1, String attr);
	/** Ermittelt den Attributwert von attr (String) der Kante e1 (int) als String
	 * @param e1 ID (int) der Kante
	 * @param attr Attributname (String)
	 * @return Wert (String) des Attributes | Leerer String im Fehlerfall
	 */
	String getStrE(int e1, String attr);
	/** Ermittelt den Attributwert von attr (String) der Ecke v1 (int) als String
	 * @param v1 ID (int) der Ecke
	 * @param attr Attributname (String)
	 * @return Wert (String) des Attributes | Leerer String im Fehlerfall
	 */
	String getStrV(int v1, String attr);
	/** Ermittelt alle Attribute (List(String)) der Ecke v1 (int)
	 * @param v1 ID (int) der Ecke
	 * @return Liste (List(String)) aller Attributnamen
	 */
	List<String> getAttrV(int v1);
	/** Ermittelt alle Attribute (List(String)) der Kante e1 (int)
	 * @param e1 ID (int) der Ecke
	 * @return Liste (List(String)) aller Attributnamen
	 */
	List<String> getAttrE(int e1);
	
	//Mutatoren
	/** Setzt den Attributwert val (int) von attr (String) der Kante e1 (int) auf val (int)
	 * @param e1 ID (int) der Kante
	 * @param attr Name (String) des Attributes
	 * @param val Neuer Wert (int) des Attributes
	 */
	void setValE(int e1, String attr, int val);
	/** Setzt den Attributwert val (int) von attr (String) der Ecke v1 (int) auf val (int)
	 * @param v1 ID (int) der Ecke
	 * @param attr Name (String) des Attributes
	 * @param val Neuer Wert (int) des Attributes
	 */
	void setValV(int v1, String attr, int val);
	/** Setzt den Attributwert val (String) von attr (String) der Kante e1 (int) auf val (String)
	 * @param e1 ID (int) der Kante
	 * @param attr Name (String) des Attributes
	 * @param val Neuer Wert (String) des Attributes
	 */
	void setStrE(int e1, String attr, String val);
	/** Setzt den Attributwert val (String) von attr (String) der Ecke v1 (int) auf val (String)
	 * @param v1 ID (int) der Ecke
	 * @param attr Name (String) des Attributes
	 * @param val Neuer Wert (String) des Attributes
	 */
	void setStrV(int v1, String attr, String val);
	/** Print-Methode für Graphen
	 */
	void print();
	/**Liefert den Wert ob der Graph directed ist
	 * @return boolean des directed Atributs
	 */
	boolean directed();
	/**Setzt den Attributwert val (boolean) des Graphen
	 * @param val (boolean) des Graphen
	 */
	void setDirected(boolean val);
}
