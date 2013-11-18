/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Uebungsaufgabe_2_1;

import java.util.Random;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import static com.google.common.base.Preconditions.*;

/**
 *
 * @author abk640
 */
class MatrixArray extends MatrixImpl {

    private int zeilen;
    private int spalten;
    private double[][] matrix;

//    public int zaehler=0;

    public int getZaehler(){
        return this.zaehler;
    }

    public Matrix init(int zeilen, int spalten, Collection<Collection<Double>> coll) {
        return new MatrixArray(zeilen, spalten, coll);
    }

    public Matrix init(int n){
        return new MatrixArray(n,n);
    }

    public Matrix init(int zeilen, int spalten){
        return new MatrixArray(zeilen, spalten);
    }

    public MatrixArray(int zeilen, int spalten) {
        this.matrix = new double[zeilen][spalten];
        this.zeilen = zeilen;
        this.spalten = spalten;
    }

    public MatrixArray(int zeilen, int spalten, Collection<Collection<Double>> coll) {  //m * n WerteCollection
        this.matrix = new double[zeilen][spalten];
        checkArgument(coll.size() == zeilen);
        for (Collection<Double> c : coll) {
            checkArgument(c.size() == spalten);
        }
        this.zaehler = 0;
        this.spalten = spalten;
        this.zeilen = zeilen;
        Iterator<Collection<Double>> iterateColl = coll.iterator();
        for (int i = 0; i < zeilen; i++) {
            zaehler += 2;
            Collection<Double> zeile = iterateColl.next();
            Iterator<Double> iterateZeile = zeile.iterator();
            for (int j = 0; j < spalten; j++) {
                double d = iterateZeile.next();
                matrix[i][j] = d;
                zaehler += 1;
            }
        }
    }
    /**
     * Gibt die maximale Anzahl an Zeilen zurück, die die Matrix hat.
     * @return = maximale Anzahl von Zeilen
     */
    public int getZeilen() {
        return this.zeilen;
    }
    /**
     * Gibt die maximale Anzahl an Spalten zurück, die die Matrix hat.
     * @return = maximale Anzahl von Spalten
     */
    public int getSpalten() {
        return this.spalten;
    }
    /**
     * Gibt den spezifischen Wert zurück, der an der Stelle (zeile,spalte) steht. Liegt zeile
     * oder spalte nicht innerhalb der Matrix, dann wird eine Exception geworfen.
     * @param zeile = Zeilenangabe
     * @param spalte = Spaltenangabe
     * @return = Gibt die Reelle Zahl der an der Stellt (zeile,spalte) zurück.
     */
    public double getValue(int zeile, int spalte) {
        checkArgument(zeile<=this.getZeilen() && zeile>= 0);
        checkArgument(spalte<=this.getSpalten() && spalte >= 0);
        zaehler+=1;
        return this.matrix[zeile][spalte];
    }
    /**
     * Schreibt einen spezifischen Wert value an die Stelle (zeile,spalte).
     * @param zeile = Zeilenangabe
     * @param spalte = Spaltenangabe
     * @param value = Zu schreibender Wert
     */
    public void setValue(int zeile, int spalte, double value) {
        zaehler+=1;
        this.matrix[zeile][spalte] = value;
    }
    /**
     * Standard toString Methode, um die Matrix in einen String zu konvertieren.
     * Dazu wird ein leerer String angelegt, der mit jeder Zeile gefüllt wird.
     * @return = Ein String der Matrix
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < this.getZeilen(); i++) {
            for (int j = 0; j < this.getSpalten(); j++) {
                s = s + this.matrix[i][j] + " ";
            }
        }
        return s;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Arrays.deepHashCode(this.matrix);
        return hash;
    }
    
    public int size(){
        return this.getSpalten()*this.getZeilen();
    }

}
