package A1;

/** Implementation für verkettete Listen
 *  Reicht die Anfragen nur an das Objekt "first" weiter.
 * 
 * @author Steffen Giersch, Birger Kamp, Maria Lüdemann */
public class ListeImpl implements Liste {
	
	/** Referenz auf das erste Element der Liste 
	 *  null wenn die Liste leer ist*/
	private ListElement elem = null;
	/** Zähler für die Länge der Liste */
	private int lengthCounter = 0;
	/** Zähler für Anzahl der Dereferenzierungen */
	private int stepCounter = 0;
	
	public int getStepCounter() {
		return stepCounter;
	}
	
	public void resetStepCounter() {
		stepCounter = 0;
	}

	public int length() {
		return lengthCounter;
	}

	public boolean isempty() {
		return elem == null;	//Wenn die Liste leer ist, ist das leere Element null
	}

	public void cons(Object x) {
		elem = new ListElement(x, elem);	//Erzeugen eines neuen ListeElement mit 
		stepCounter++;
		lengthCounter = lengthCounter + 1;	//dem Objekt x und dem Zeiger auf die bisherige Liste
	}

	public Object head() {
		if (elem == null) {							//Fehler werfen, wenn die Liste leer ist
			throw new NullPointerException("Liste ist leer");
		} else {
			Object element = elem.getElem();		//Das Objekt des ersten Listenelements zwischenspeichern
			elem = elem.getNext();					//Referenz auf den Rest der Liste in elem speichern um so die Liste zu verkürzen
			lengthCounter = lengthCounter - 1;		//lengthCounter um einen veringern um die Länge aktuell zu halten
			stepCounter++;							//Eine Dereferenzierung ist erfolg
			return element;
		}
	}

	public Object top() {
		stepCounter++;
		if (elem == null) {							//Fehler werfen, wenn die Liste leer ist
			throw new NullPointerException("Liste ist leer");
		} else {
			stepCounter = stepCounter + 1;			//Bevorstehende Dereferenzierung eintragen
			return elem.getElem();					//Objekt des ersten Listenelements zurückgeben
		}
	}

	public boolean insert(Object x, int n) {
		if (n > this.length()) {					//wenn n größer ist als die Liste lang ist, false zurückgeben
			return false;
		} else if (n < 0) {							//wenn n kleiner als 0 ist, false zurückgeben
			return false;
		} else if (n == 0) {						//wenn n gleich 0 ist, cons aufrufen, da das Element vorne an die Liste gefügt werden muss
			this.cons(x);
			return true;
		} else {
			lengthCounter = lengthCounter + 1;		//sonst n um einen veringern und die Aufgabe an das erste Listenelement weitergeben
			stepCounter = stepCounter + elem.insert(x, n - 1, 1);
			return true;
		}
	}
}
