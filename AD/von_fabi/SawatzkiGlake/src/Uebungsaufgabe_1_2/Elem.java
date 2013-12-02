/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Uebungsaufgabe_1_2;

import java.util.LinkedList;

/**
 *
 * @author abk640
 */
public class Elem{

    private Object obj;     //Im Element gespeicherter Wert.
    private Elem next;      //Zeiger, auf welches naechste Element das aktuelle zeigen soll.

    /**
     * Konstruktor zur Erzeugung eines Elements.
     * @param obj = Wert des neuen Elements, kann ein beliebiges Objekt sein.
     */
    public Elem(Object obj) {
        this.obj = obj;
    }
    /**
     * Getter- Methode zur Rueckgabe des tatsaechlichen Wertes eines Elements.
     * @return = Gibt den Wert, des aktuelle Elements zurueck.
     */
    public Object obj() {
        return this.obj;
    }
    /**
     * Gibt das Eelement zurueck, auf das gezeigt wird.
     * @return = Gibt ein Element zurueck, auf das als naechstes gezeigt wird.
     */
    public Elem next() {
        return this.next;
    }
    /**
     * Setz den Zeiger auf das naechste Element auf das uebergebene Element.
     * @param e = Das neue Element, auf das gezeigt werden soll.
     */
    public void setNext(Elem e) {
        this.next = e;
    }
    /**
     * Standardmaeßige equals Methode zur Ueberpruefung auf Wertgleichheit eines
     * Elements und eines beliebigen Objekts.
     * @param obj = Zu ueberpruefendes Obejtk (Any)
     * @return = Gibt TRUE zurueck, sobald der Inhalt beider Operanden gleich ist, ansonsten FALSE.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Elem)) {
            return false;
        } else {
            Elem e = (Elem) obj;
            if (e.obj() != this.obj()) {
                return false;
            }
        }
        return true;
    }
    /**
     * Standardmaeßige toString Methode, um ein Element in einen String zu konvertieren.
     * @return = Gibt das Element in Form eines String zurueck.
     */
    @Override
    public String toString() {
        return this.obj.toString();
    }

}
