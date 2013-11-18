/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

import java.util.Vector;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.Double;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import static com.google.common.base.Preconditions.*;
/**
 *
 * @author abk640
 */
public class MatrixArrayList extends MatrixImpl{

    private int zeilen;
    private int spalten;
    private List<Tupel>[] matrix;

//    private int zaehler= 0;
    /**
     * Getter- Methode zur Rueckgabe des Dereferenzierungs- Zaehlers.
     * @return = Gibt den Dereferenzierungs- Zaehler als Integer zurueck.
     */
    public int getZaehler(){
        return this.zaehler;
    }

    public Matrix init(int n){
        return new MatrixArrayList(n, n);
    }
    /**
     * Initialisierungsmethode zur Erezeugung einer Matrix, mit bestimmten Werten.
     * Ist die Groesse der Collection, nicht gleich zur Groesse der Matrix, wird eine 
     * Exception geworfen.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten = Anzugebende Spaltengroesse
     * @param coll = Einzutragende Werte, in die Matrix
     * @return = Gibt eine neue Matrix zurueck, die nach den Paramtern aufgebaut wurde.
     */
    public Matrix init(int zeilen, int spalten, Collection<Collection<Double>> coll){
        return new MatrixArrayList(zeilen, spalten, coll);
    }

    public Matrix init(int zeilen, int spalten){
        return new MatrixArrayList(zeilen, spalten);
    }
    /**
     * MatrixArrayLis Konstruktor zur Erzeugung eines Matrix Objekt, mit Array Zeilen und Listen
     * als Spalten, nach einer bestimmten Groesse. 
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten  = Anzugebende Spaltengroesse
     */
    public MatrixArrayList(int zeilen, int spalten){
        this.zeilen=zeilen;
        this.spalten=spalten;
        this.matrix = new List[zeilen];
        for(int i=0;i<zeilen;i++){
            this.matrix[i] = new LinkedList();
        }
    }
    /**
     * MatrixArrayList Konstruktor zur Erzeugung eines Matrix Objekts, mit Zeilen als Array und Spalten als Listen,
     * die eine bstimmte Groesse haben und mit vordefinierten Werten befüllt wird. Sollte die Collection, nicht
     * der Groesse der neuen Matrix entsprechend, wird eine Exception geworfen.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten = Anzugebende Spaltengroesse
     * @param coll = Collection, aus der die Werte in die Matrix übernommen werden.
     */
    public MatrixArrayList(int zeilen, int spalten, Collection<Collection<Double>> coll){
        this.matrix = new List[zeilen];
        checkArgument(coll.size()==zeilen);
        for(Collection<Double> c:coll){
            checkArgument(c.size() == spalten);
        }
        this.spalten = spalten;
        this.zeilen = zeilen;
        Iterator<Collection<Double>> iterateColl  = coll.iterator();
        for(int i=0;i<zeilen;i++){
            this.matrix[i] = new LinkedList<Tupel>();
            Collection<Double> zeile = iterateColl.next();
            Iterator<Double> iterateZeile = zeile.iterator();
            int j=0;
            while(iterateZeile.hasNext()){
                double wert = iterateZeile.next();
                if(wert != 0.0){
                    zaehler +=1;
                    this.matrix[i].add(new Tupel(j,wert));
                }
                j+=1;
            }
        }
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.deepHashCode(this.matrix);
        return hash;
    }

    public double getValue(int i, int j) {
        checkArgument(i <= zeilen && j <= spalten);
        List<Tupel> zeile = this.matrix[i];
        for(Tupel e: zeile){
            zaehler+= 1;
            if(e._1() == j){
                return e._2();
            }
        }
        return 0.0;
}

    public void setValue(int i, int j, double value) {
        checkArgument(i <= this.zeilen && j<=this.spalten);
        int zaehler1 = 0;

        if(value != 0.0){
            for(Tupel t: this.matrix[i]){
                if(t._1() == j){
                    this.matrix[i].set(zaehler1, new Tupel(j,value));
                }
                 this.zaehler+=1;
                zaehler1 = zaehler1 + 1;
            }
            this.matrix[i].add(new Tupel(j, value));
        }
    }

    public int getZeilen() {
        return this.zeilen;
    }

    public int getSpalten() {
       return this.spalten;
    }

    public int size(){
        int size = 0;
        for(int i=0; i< this.getZeilen(); i++){
            size += this.matrix[i].size();
        }
        return size;
    }
}
