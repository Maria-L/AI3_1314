/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Uebungsaufgabe_1_2;

import java.util.ListIterator;
import java.util.List;
import static com.google.common.base.Preconditions.*;

/**
 *
 * @author abk640
 */
public final class ListeImpl implements Liste {

//    private Elem tail = null;
    private Elem kopf = null;
    private Elem last = null;
    private Elem cursor = null;
    private int hilfsvariable = 0;

    /**
     * Erstellt ein neue ListeImpl Liste, die mit den Werte aus der
     * übergebenen Liste befüllt wird.
     * @param list = Werte, die in die neue List übernommen werden.
     */
    public ListeImpl(List<?> list) {
        ListIterator iterateList = list.listIterator(list.size());
        while (iterateList.hasPrevious()) {
            Object obj = iterateList.previous();
            this.cons(obj);
        }
    }

    /**
     * Erzeugt eine neue ListeImpl Liste, die leer ist
     * @param obj
     */
    public ListeImpl(Object obj) {
        this.cons(obj);
    }

    /**
     * Erezeugt eine neues Listen Objekt ListeImpl.
     */
    public ListeImpl() {
    }

    /**
     * Getter- Methode zum Zurueckgeben des Dereferenzierungs- Zaehlers.
     * @return = Gibt den Dereferenzierungs Zaehler als Integer zurueck.
     */
    public int referenzenCounter() {
        return this.hilfsvariable;
    }

    /**
     * Standardmäßige equals Methode zum Vergleich von einer List mit einem Any Objekt.
     * Wenn beide denselben Inhalt besitzen, dann wird TRUE zurückgeliefert, ansonsten FALSE.
     * @param obj = Zu vergleichendes ANY Objekt.
     * @return = Liefert TRUE zurück, wenn der Inhalt beider verlgeichenden Operanden gleich ist, ansonsten FALSE.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Liste)) {
            return false;
        } else {
            Liste l = (Liste) obj;
            if (l.length() != this.length()) {
                return false;
            }
            Elem cursor1 = l.getKopf();
            Elem cursor2 = this.getKopf();
            while (cursor2 != null) {
                if (!(cursor2.equals(cursor1))) {
                    return false;
                }
                cursor1 = cursor1.next();
                cursor2 = cursor2.next();
            }
        }
        return true;
    }

    /**
     * Gibt das erste Element, der List zurück.
     * @return = Erstes Element der Liste
     */
    public Elem getKopf() {
        return this.kopf;
    }

    /**
     * Funktion, zum Zurückstellen des Referenz- Zaehlers.
     */
    public void resetReferenzenCounter() {
        this.hilfsvariable = 0;
    }

    /**
     * Standardmaeßige toString Methode, um die Liste in eine nutzbaren String
     * umzukonvertieren.
     * @return = Gibt die List in Form eines String zurueck.
     */
    @Override
    public String toString() {
        String string = "";
        cursor = kopf;
        while (cursor != null) {
            string = string + cursor.obj();
            cursor = cursor.next();
            hilfsvariable = hilfsvariable + 1;
        }
        return string;
    }

    /**
     * Haengt ein Element x an die vorderste Stelle der Liste und markiert dieses
     * als neues Kopfelement.
     * @param x = Anzuhaengender Wert
     */
    public void cons(Object x) {
        checkNotNull(x);
        checkArgument(!(x instanceof Elem));
        if (x != null) {
            Elem elem = new Elem(x);
            Elem temp;
            if (!this.isEmpty()) {
                temp = kopf;
                kopf = elem;
                elem.setNext(temp);
                this.hilfsvariable += 1;
            } else {
                this.kopf = elem;
                this.last = elem;
                elem.setNext(null);
            }
        }
    }

    /**
     * Entfernt das vorderste Element einer Liste.
     */
    public void head() {
        checkNotNull(this.isEmpty());
        kopf = kopf.next();
    }

    public void getTail() {
        if (!(this.isEmpty())) {
            this.kopf = this.kopf.next();
        }
    }

    /**
     * Gibt die Laenge einer Liste zur. Laenge der Liste wird bestimmt durch
     * die, in der Liste gespeicherten Elemente.
     * @return = Gibt eine ganzzahlige Laenge einer Liste zurueck
     */
    public int length() {
        int sum = 0;
        cursor = kopf;
        while (cursor != null) {
            sum = sum + 1;
            cursor = cursor.next();
            hilfsvariable = hilfsvariable + 1;
        }
        return sum;
    }

    /**
     * Prueft, ob die Liste leer ist.
     * @return = Gibt TRUE zurueck, wenn sie leer, ansonsten FALSE
     */
    public boolean isEmpty() {
        return (kopf == null);
//        int length = this.length();
//        return (length == 0);
    }

    /**
     * Fuegt an eine spezifische Stelle in der Liste ein Element an.
     * Dazu iteriert es bis an die Stelle, wo er es einsetzen möchte und
     * versetze den Fokus, des Pointer, des vorherigen Elements auf das neue
     * Element. Der Fokus des Pointers, des neuen Elements wird auf das nachfolgende
     * Element gesetzt.
     * @param x = Einzufuegendes Element
     * @param n = Stelle, an der das neue Element eingefuegt werden soll
     * @return = Gibt TRUE zuruekc, sobald das Einfuegen erfolgreich war
     */
    public boolean insert(Object x, int n) {
        checkNotNull(x);
        if (n <= 0) {
            this.cons(x);
            return true;
        } else {
            Elem elem = new Elem(x);
            Elem temp;
            cursor = kopf;
            while (n > 1) {
                if (cursor == null) {
                    return false;
                }
                n = n - 1;
                hilfsvariable = hilfsvariable + 1;
                cursor = cursor.next();
            }
            elem.setNext(cursor.next());
            cursor.setNext(elem);
            return true;
        }
    }

    /**
     * Einfüge- Methode, mittels eines rekursiven Ansatzes, bei dem Wete, die vor dem gewählten n liegen
     * in einer Variable zwischengespeichert werden und nach verlassen der Methode wieder vorne
     * an die neue Liste angefügt werden. Die Laufzeit dieser Methode ist : O(n), da die Liste im schlimmsten
     * Fall einmal ganz durchlaufen werden muss, um ein neues Element anzfügen.
     * @param obj = Das abzuspeichernde Objekt.
     * @param n = Die n-te Stelle, an der obj gespeichert werden soll.
     */
    public void ins(Object obj, int n) {
        checkNotNull(n);
        hilfsvariable += 1;
        if (n <= 0) {
            this.cons(obj);
        } else {
            Elem head = this.getKopf();

            this.getTail();

            this.ins(obj, n - 1);

            if (head != null) {
                this.cons(head.obj());
            }
        }
    }

//    public boolean isSorted(){
//        cursor = this.getKopf();
//        while(cursor != null){
//            if(cursor > cursor.next()){
//                return false;
//            }
//        }
//        return true;
//    }

    public Liste sort(){
        Liste rightListe = new ListeImpl();
        Liste leftListe = new ListeImpl();
        Elem kopfR = this.getKopf();
        int counter = 0;
        while(counter != Math.round(this.length()/2)){
            leftListe.cons(kopfR.obj());
            kopfR = kopfR.next();
            counter += 1;
        }
        while(kopfR != null){
            rightListe.cons(kopfR.obj());
            kopfR = kopfR.next();
        }
        return merge(leftListe.sort(),rightListe.sort());
    }

    public Liste getPart(int n){
        checkArgument(n >= 0);
        checkArgument(n <= this.length());
        Liste partitionList = new ListeImpl();
        cursor = this.getKopf();
        while(n >= 0){
            partitionList.cons(cursor.obj());
            cursor = cursor.next();
        }
        return partitionList.reverse();
    }

//    public Liste mergesort(Liste right){
//        int l= this.length();
//        int r = right.length();
//        if(l < r){
//            int mitte = Math.round((l+r)/2);
//            this.mergesort(this.getPart(mitte));
//            Liste right1 = right.getPart((mitte + 1));
//            right.mergesort(right1);
//        }
//        return right;
//    }

    public Liste reverse(){
        Liste reverseList = new ListeImpl();
        cursor = this.getKopf();
        while(cursor != null){
            reverseList.cons(cursor.obj());
            cursor = cursor.next();
        }
        return reverseList;
    }

    private Liste merge(Liste leftListe, Liste rightListe) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
