/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_2;

import Uebungsaufgabe_1_1.Messung;
import Uebungsaufgabe_1_1.Messung1;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author abk640
 */
public class ListeTest {

    /**
     * Wir testen noch einmal wild mit allen Listenfunktionen, um zu gewährleisten,
     * dass diese auch in Kombination korrekt arbeiten.
     */
    @Test
    public void testCombined() {
        Liste testListe = new ListeImpl();
        testListe.cons(1);
        testListe.cons(2);
        testListe.cons(3);
        testListe.insert(4, 1);
        testListe.head();
        testListe.cons(5);
        assertEquals(testListe, new ListeImpl(Arrays.asList(5,4,2,1)));
    assertEquals(new ListeImpl('a'),new ListeImpl(Arrays.asList('a')));
    }


  /**
   * Aufgabe 6:
   * Hypothese: Wir erwarten ein Laufzeit, die proportinal zur Anzahl der einzufügenden Elemente ist.
   * Bei unseren 1000000 Elemente würde unser counter also 1000000 Referenzen verfolgen, da wird nur
   * mit unserem Kopf Zeiger arbeiten.
   * Ergebnis: Die Funktion cons() liegt wie erwartet in O(n)
   */
    @Test
    public void testTask6(){
        List experimentList = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        ListeImpl liste = new ListeImpl(experimentList);
        assertEquals("1234567890",liste.toString());
        ListeImpl liste2 = new ListeImpl();
        for(int i=0; i<=1000000;i++){
            liste2.cons(i);
        }
        System.out.println("Ergebnis zu Aufgabe 6: " +liste2.referenzenCounter());
    }

    /**
     * Aufgabe 7:
     * Test  zur Aufgabe 7 der Listenimplementation.
     * Hypothese: Um die Elemente an das Ende unserer Liste anzufügen, wird einfach
     * unsere isnert() Methode verwendet, die als Ziel das Einfügen an der letzen Stelle
     * hat. Wir nehmen an, dass die Laufzeit durch das ewige Iterieren an die letze Stelle in O(n²) liegen
     * muss. Denn für n Elemente muss n mal über die Liste iteriert werden, auch wenn die Liste
     * am Anfang noch nicht ihre maximale Größe erreicht hat.
     * Ergebnis: Der Referenzen-Zähler bestätigt unsere Annahme
     */
    @Test
    public void testTask7(){
        ListeImpl liste = new ListeImpl();
        for(int i=0; i<=10000;i++){
            liste.insert(i, liste.length()-1);
        }
        System.out.println("Ergebnis zu Aufgabe 7: " + liste.referenzenCounter());
    }
    /**
     * Aufgabe 8:
     * Test zu Aufgabe 8
     * Hypothese: Da wir erneut mit der insert-Methode arbeiten, erwarten wir ein ähnliches
     * Ergebnis wie bei der Aufgabe 7, allerdings fügen wir die Elemente dieses Mal nicht immer
     * an das Ende der Liste an, sondern irgendwo, weshalb der tatsächliche Zeitaufwand in Form
     * der Dereferenzierungen etwas niedriger sein sollte, als bei Aufgabe 7 (Stichwort Konstante)
     * Dennoch erwarten wir ein Ergebnis in O(n²). Die Varianz der Laufzeit wird hoch sein, weil
     * wir das Element an einer beliebigen Stelle einfügen. Je nachdem wie hoch die Stelle ist, wird
     * die Laufzeit auch verändert.
     * Ergebnis: Der Referenzen-Zähler bestätigt unsere Annahme. Auch wenn die Funktion letztendlich in
     * der O-Klasse von O(n²) liegt, ist das Ergebnis aufgrund der günstigeren Konstante deutlich besser als es
     * bei Aufgabe 7 der Fall ist.
     */
    @Test
    public void testTask8(){
        List<Long> timeList = new ArrayList();
        Random random = new Random();
        Messung1 messungen1 = new Messung1();
        for(int j=0; j<15;j++){
            ListeImpl liste = new ListeImpl();
            long old = System.currentTimeMillis();
            for(int i=0; i<= 10000; i++){
                int r = (int) (Math.random() * liste.length());
                liste.insert(i,r);
                
            }
//            System.out.println(liste.referenzenCounter());
            messungen1.add(liste.referenzenCounter());
            long yet = System.currentTimeMillis();
            timeList.add((yet-old));
        }
//        System.out.println(timeList);
        Messung1 messungen = new Messung1();
        for(long time: timeList){
            messungen.add(time);
        }
        System.out.println("Ergebnis zu Aufgabe 8(Mittelwert): " + messungen1.mittelwert());
        System.out.println("Ergebnis zu Aufgabe 8(Varianz): " + messungen1.varianz());
        System.out.println("Systemzeit zu Aufgabe 8 - Mittelwert: " + messungen.mittelwert());
        System.out.println("Systemzeit zu Aufgabe 8 - Varianz: " +messungen.varianz());
    }




    /**
     * Die Cons Methode muss ein Element vorne an die Liste anfügen können. Den Kopf
     * neu setzen und die Referenz auf das nächste Objekt erstellen.
     */
    @Test
    public void testCons() {
        Liste testListe = new ListeImpl();
        testListe.cons(1);testListe.cons(2);testListe.cons(3);
        Elem e1 = new Elem(3);
        Elem e2 = new Elem(2);
        e2.setNext(new Elem(1));
        e1.setNext(e2);
        assertEquals(testListe,new ListeImpl(Arrays.asList(3,2,1)));
        assertEquals(e1, testListe.getKopf());
    }
    /**
     * Es darf nicht möglich sein, ein Null-Objekt in die Liste einzufügen. Ansonsten wäre
     * es möglich, dass Elemente mitten in der Liste auf null zeigen, was semantisch keinen Sinn ergibt.
     */
    @Test(expected = NullPointerException.class)
    public void testConsException(){
        Liste testListe = new ListeImpl();
        testListe.cons(null);
    }
    /**
     * Die Head-Methode muss das vorderste Element entfernen und den Head neu setzen.
     */
    @Test
    public void testHead() {
        Liste testListe = new ListeImpl();
        testListe.cons(1);
        testListe.cons(2);
        testListe.cons(3);
        testListe.head();
        Elem e2 = new Elem(2);
        e2.setNext(new Elem(1));
        assertEquals(testListe,new ListeImpl(Arrays.asList(2,1)));
        assertEquals(e2,testListe.getKopf());
    }
    /**
     * Es ist logischerweise nicht möglich, aus einer leeren Liste das vorderste Element zu entfernen.
     */
    @Test(expected = NullPointerException.class)
    public void testExceptionHead() {
        Liste testListe = new ListeImpl();
        testListe.head();
    }

    /**
     * Die Länge der Liste muss abhängig von den cons-, head- und insert-Aufrufen inkrementiert,
     * bzw. dekrementiert werden.
     */
    @Test
    public void testLength() {
         Liste testListe = new ListeImpl();
        testListe.cons(1);
        testListe.cons(2);
        testListe.cons(3);
        testListe.head();
        testListe.insert('s', 1);
        assertEquals(3,testListe.length());
    }

    /**
     * Nur eine leere Liste darf in diesem Fall true zurückliefern. Der Grenzfall,
     * dass mal etwas in der Liste gewesen ist, wird ebenfalls betrachtet.
     */
    @Test
    public void testIsEmpty() {
        Liste testListe = new ListeImpl();
        testListe.cons(1);
        assertEquals(false,testListe.isEmpty());
        testListe.head();
        assertEquals(true, testListe.isEmpty());
    }

    /**
     * Es muss ein Element an einer beliebigen Stelle eingefügt werden können.
     * Die Referenzen und evtl. auch der Kopf müssen dann korrekt gesetzt werden.
     */
    @Test
    public void testInsert() {
       Liste testListe = new ListeImpl();
       testListe.cons(1);
       testListe.cons(2);
       testListe.cons(3);
       testListe.insert('s', 1);
       assertEquals(new ListeImpl(Arrays.asList(3,'s',2,1)),testListe);
    }
    /**
     * Auch in diesem Fall muss verhindert werden, dass ein Null-Objekt hinzugefügt wird.
     */
    @Test(expected = NullPointerException.class)
    public void testExceptionInsert(){
        Liste testListe = new ListeImpl();
        testListe.insert(null, 2);
    }

    @Test
    public void testInsertRecursive(){
        Liste testListe = new ListeImpl();
        testListe.cons(3);
        testListe.cons(2);
        testListe.cons(1);
        Liste testListe1 = new ListeImpl();
        testListe1.ins(2,1);
        testListe1.ins(1,0);
        testListe1.ins(3,2);
        assertEquals(testListe,(testListe1));
    }

    @Test
    public void testRecursiveTask5_2(){
        ListeImpl liste = new ListeImpl();
        for(int i=0; i<=1000;i++){
            liste.ins(i, liste.length()-1);
        }
        System.out.println("Ergebnis zu Aufgabenblatt_5, Aufgabe_2 Rekursiv: " + liste.referenzenCounter());

    }
    @Test
    public void testIterativeTask5_2(){
        ListeImpl liste1 = new ListeImpl();
        for(int i=0; i<=1000;i++){
            liste1.insert(i, liste1.length()-1);
        }
        System.out.println("Ergebnis zu Aufgabenblatt_5, Aufgabe_2 Iterativ: " + liste1.referenzenCounter());
    }

    @Test
    public void testTask5_3(){
        ListeImpl liste1 = new ListeImpl();
        ListeImpl liste2 = new ListeImpl();
        Random random = new Random();
        for(int i=0; i<=100000;i++){
            int val = random.nextInt(100);
            liste2.ins(val,0);
            liste1.insert(val, 0);
        }
        assertEquals(liste2,liste1);

        ListeImpl liste3 = new ListeImpl();
        ListeImpl liste4 = new ListeImpl();
        Random random1 = new Random();
        for(int i=0; i<=1000;i++){
            int val = random1.nextInt(100);
            liste3.ins(val,liste3.length()-1);
            liste4.insert(val,liste4.length()-1);
        }
        assertEquals(liste3,liste4);
    }



//    @Test
//    public void testSplitting(){
//        Liste testListe = new ListeImpl('d');
//        testListe.cons('c');testListe.cons('b');testListe.cons('a');
//        Liste testListe1 = new ListeImpl('d'); testListe1.cons('c');
//        Liste testListe2 = new ListeImpl('b'); testListe2.cons('a');
//        assertEquals(testListe.sort().getKopf().obj(), testListe2);
//        assertEquals(testListe.sort().getKopf().next().obj(), testListe1);
//    }

    @Test
    public void testMerge(){

    }
}