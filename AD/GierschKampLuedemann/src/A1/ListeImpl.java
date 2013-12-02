package A1;

/** Implementation f�r verkettete Listen
 *  Reicht die Anfragen nur an das Objekt "first" weiter.
 * 
 * @author Steffen Giersch, Birger Kamp, Maria L�demann */
public class ListeImpl implements Liste {
	
	/** Referenz auf das erste Element der Liste 
	 *  null wenn die Liste leer ist*/
	private ListElement elem = null;
	/** Z�hler f�r die L�nge der Liste */
	private int lengthCounter = 0;
	/** Z�hler f�r Anzahl der Dereferenzierungen */
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
			elem = elem.getNext();					//Referenz auf den Rest der Liste in elem speichern um so die Liste zu verk�rzen
//			lengthCounter = lengthCounter - 1;		//lengthCounter um einen veringern um die L�nge aktuell zu halten
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
			return elem.getElem();					//Objekt des ersten Listenelements zur�ckgeben
		}
	}

	public boolean insert(Object x, int n) {
		if (n > this.length()) {					//wenn n gr��er ist als die Liste lang ist, false zur�ckgeben
			return false;
		} else if (n < 0) {							//wenn n kleiner als 0 ist, false zur�ckgeben
			return false;
		} else if (n == 0) {						//wenn n gleich 0 ist, cons aufrufen, da das Element vorne an die Liste gef�gt werden muss
			this.cons(x);
			return true;
		} else {
			lengthCounter += 1;		//sonst n um einen veringern und die Aufgabe an das erste Listenelement weitergeben
			stepCounter += elem.insert(x, n - 1, 0);
			return true;
		}
	}
	public String toString(){
		String accu = "[ ";
		Liste temp = this;
		for(int i=0; i < lengthCounter; i++){
			accu = accu.concat(String.valueOf(temp.head()));
			accu = accu.concat(", ");
		}
		accu = accu.concat(" ]");
		return accu;
	}
}
