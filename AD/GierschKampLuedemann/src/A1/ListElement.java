package A1;

import java.util.ArrayList;
import java.util.List;

public class ListElement {

	/** Zu speicherndes Objekt */
	private Object elem;
	/** Referenz auf das n�chste Listenelement
	 *  null, wenn kein weiteres Element folgt */
	private ListElement next;

	/**
	 * Constructor f�r das ListElement
	 * @param x    Zu speicherndes Objekt
	 * @param tail Referenz auf den Rest der Liste
	 */
	ListElement(Object x, ListElement tail) {
		elem = x;
		next = tail;
	}

	/**
	 * Gibt das gespeicherte Objekt zurueck
	 * @return Gespeichertes Objekt
	 */
	public Object getElem() {
		return elem;
	}

	/**
	 * Gibt die Referenz auf den Rest der Liste zurueck
	 * @return Referenz auf den Rest der Liste
	 */
	public ListElement getNext() {
		return next;
	}

	/**
	 * Wenn n == 0 ist, dann wird das Objekt x hinter dieses ListElement gefuegt.
	 * Ansonsten wird die Aufgabe an das Folgende ListElement weitergereicht und
	 * n um 1 dekrementiert.
	 * @param x Zu Speicherndes Objekt
	 * @param n Indexzaehler fuer den Speicherort
	 * @param i Dereferenzierungs-Zaehler - Hilfsvariable fuer ListeImpl
	 * @return Anzahl der Dereferenzierungen
	 */
	public int insert(Object x, int n, int counter,ListeImpl datList) {
		List<Object> oldValues = new ArrayList<Object>();
		for(int i=0 ; i<n ; i++){
			oldValues.add(elem);
			elem = next.getElem();
			counter++;
			next = next.getNext();
		}
		next = new ListElement(x,next);
		elem = x;
		for(Object o : oldValues){
			next = new ListElement(elem,next);
			elem = o;
		}
		return counter;
	}

}
