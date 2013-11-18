/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Uebungsaufgabe_2_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import static com.google.common.base.Preconditions.*;

/**
 *
 * @author abk640
 */
public class MatrixList extends MatrixImpl {

    private int zeilen;
    private int spalten;
    private List<Tripel> matrix;

//    public int zaehler = 0;
    /**
     * Getter- Methode zur Rueckgabe des Dereferenzierungs Zaehlers.
     * @return = Gibt den Dereferenzierungs Zaehler als Integer zurueck.
     */
    public int getZaehler() {
        return this.zaehler;
    }
    /**
     * MatrixList Konstruktor zur Erzeugung einer Matrix mit einer Liste als ganze Struktur
     * fuer die Matrix, die nach einer bestimmten Groesse aufgebaut wird.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten = Anzugebende Spaltengroesse
     * @return = Gibt eine neue Matrix zurueck, die nach den Paramtern aufgebaut wurde.
     */
    public Matrix init(int zeilen, int spalten) {
        return new MatrixList(zeilen, spalten);
    }

    public Matrix init(int n) {
        return new MatrixList(n, n);
    }
    /**
     * Initialisiert eine neue Liste, die nach einer bestimmten Groesse aufgebaut ist und
     * mit vorgefertigten Werte befuellt wird. Ist die Groesse der Collection ungleich der Groesse
     * der neuen Matrix, dann wird eine Exception geworfen.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten = Anzugebende Spaltengroesse
     * @param coll = Enthaelt Werte, die in die neue Matrix uebernommen werden.
     * @return = Gibt eine neue Matrix zurueck, die nach den Parametern aufgebaut wurde.
     */
    public Matrix init(int zeilen, int spalten, Collection<Collection<Double>> coll) {
        return new MatrixList(zeilen, spalten, coll);
    }
    /**
     * MatrixList Konstruktor zur Erzeugung eines neuen Matrix Objekts, die mit nur einer
     * Liste als Struktur realisiert wird und nach einer bestimmten Groesse aufgebaut wird.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten  = Anzugebende Spaltengroeese
     */
    public MatrixList(int zeilen, int spalten) {
        this.zeilen = zeilen;
        this.spalten = spalten;
        this.matrix = new LinkedList<Tripel>();
    }
    /**
     * MatrixList Konstruktor zur Erzeugung eines Matrix Objekts, welches nach einer 
     * bestimmten Groesse aufgebaut wird und mit Werten, aus der uerbgebenen Collection
     * befuellt wird. Ist die Groesse der Collection ungleich, der neuen Matrixgroesse wird
     * eine Exception geworfen.
     * @param zeilen = Anzugebende Zeilengroesse
     * @param spalten = Anzugebende Spaltengroesse
     * @param coll = Gibt eine neue Matrix zurueck, die nach den Parametern aufgebaut wurde.
     */
    public MatrixList(int zeilen, int spalten, Collection<Collection<Double>> coll) {
        checkArgument(coll.size() == zeilen && zeilen >= 0 && spalten >= 0);
        for (Collection<Double> c : coll) {
            checkArgument(c.size() == spalten);
        }
        this.zeilen = zeilen;
        this.spalten = spalten;
        this.matrix = new LinkedList<Tripel>();
        Iterator<Collection<Double>> iterateColl = coll.iterator();
        for (int i = 0; i < zeilen; i++) {
            Collection<Double> zeile = iterateColl.next();
            Iterator<Double> iterateZeile = zeile.iterator();
            int j = 0;
            while (iterateZeile.hasNext()) {
                double wert = iterateZeile.next();
                if (wert != 0.0) {
                    zaehler += 1;
                    this.matrix.add(new Tripel(i, j, wert));
                }
                j += 1;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.matrix != null ? this.matrix.hashCode() : 0);
        return hash;
    }

    public double getValue(int zeile, int spalte) {
        checkArgument(zeile <= this.zeilen && spalte <= this.spalten);
        for (Tripel e : this.matrix) {
            zaehler += 1;
            if (e._1() == zeile && e._2() == spalte) {
                return e._3();
            }
        }
        return 0.0;
    }

    public void setValue(int zeile, int spalte, double value) {
        checkArgument(zeile <= this.zeilen && spalte <= this.spalten);
        int zaehler1 = 0;
        if (value != 0.0) {
            for (Tripel e : this.matrix) {
                zaehler += 1;
                if (e._1() == zeile && e._2() == spalte) {
                    this.matrix.set(zaehler1, new Tripel(zeile, spalte, value));
                }
                zaehler1 += 1;
            }
            this.matrix.add(new Tripel(zeile, spalte, value));
        }
    }

    public int getZeilen() {
        return this.zeilen;
    }

    public int getSpalten() {
        return this.spalten;
    }

    public int size() {
        return this.matrix.size();
    }
}
