/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_2;

/**
 * Praktikumsgruppe_2
 * Aufgabe_1
 * @author Fabian Sawatzki, Daniel Glake
 */
public interface Liste {

/**
 * Konkateniert ein neues Element an die vorderste Stelle der Liste. Dazu wird
 * ein neues Element erzeugt, in der der Wert abgespeichert wird. Die Referenz vom
 * vorherigen ersten Element wird dann auf das neue Element gesetzt, während das neue
 * Element auf das alte zeigt.
 * @param x = Wert des anzuhängenen Elementes
 */
public void cons(Object x);
/**
 * Entfernt das vorderste Element an der List. Dazu entfernt es einfach
 * die Referenz auf das vorderste Element und setzt diese dann auf das nächste.
 */
public void head();
/**
 * Iteriert einmal über die gesamte Liste, um die Anzahl von Elementen zu zählen.
 * Hat somit eine Laufzeit von O(n).
 * @return = Gibt die Länge der Liste als Integer zurück.
 */
public int length();
/**
 * Überprüft, ob an der Kopf Zeiger auf irgendein Element zeigt, dass nicht null ist.
 * @return = Wenn auf kein Element gezeigt wird dann TRUE, ansonsten FALSE.
 */
public boolean isEmpty();
/**
 * Fügt an einer bestimmten Stelle, der List ein neues Element ein. Dazu iteriert es einmal
 * durch die List und inkrementiert einene Zaehler bis der gewünschte Ort gefunden ist.
 * Anschließen wird der Zeiger des vorderen Elements auf das neue Element gelenkt, während
 * das neue Element auf das hinter zeigt. Ist der gewünschte Platz Nr. 0 wird jedoch einfach
 * cons() aufgerufen.
 * @param x = Der Wert, des neuen Elements
 * @param n = Die Stelle, an der das neue Element eingefügt werden soll.
 * @return = Gibt ein TRUE zurück, sobald alles erfolgreich abgeschlossen ist, ansonsten FALSE.
 */
public boolean insert(Object x, int n);
/**
 * Getter- Methode um das vorderste Element zurück zu geben.
 * @return = Gibt das vorderste Element zurück
 */
public Elem getKopf();
/**
 * Standardmäßige equals Methode, die eine Liste und ein Any Objekt verlgleicht,
 * ob sie vom Inhalt her gleich sind.
 * @param obj = Zu verlgeichendes Objekt.
 * @return = Gibt TRUE zurück, sobald beide Objekte inhaltlich gleich sind, ansonsten FALSE
 */
    @Override
public boolean equals(Object obj);
/**
 * Methode, zum Einfügen neuer Elemente an einer bestimmte Stelle in der Liste,
 * mittels eiens rekursiven Ansatzes. Listes wird dabei in Kopf und Tail
 * getrennt. Kopf wird gespeichert und ins() wird mit tail aufgerufen, bis die gewünschte Stelle erreicht wurde.
 * @param obj = Das abzuspeichernde Objekt
 * @param n = Stelle, an der obj eingefügt werden soll
 */
public void ins(Object obj, int n);

/**
 * Metode, zum Sortiren der Liste, über Divide and Conquer. Neue Liste wird
 * zurückgegeben.
 */
public Liste sort();

/**
 * Methode, die eine neue Liste erzeugt, dessen Elemente in umgekehrter Reihenfolge eingefügt
 * wird. Laufzeit von O(n).
 * @return = Gibt eine neue Liste zurück
 */
public Liste reverse();
//public Liste mergesort(Liste right);
//public Liste getPart(int i);
}
