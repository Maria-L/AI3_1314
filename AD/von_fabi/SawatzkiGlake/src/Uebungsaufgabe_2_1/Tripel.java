/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

/**
 *
 * @author abk640
 */
public class Tripel{

    private int first;
    private int second;
    private double third;

    /**
     * Tripel Konstruktor zur Erzeugung eines Tripels
     * @param i = Wert fuer die erste Stelle des Tripels
     * @param j = Wert fuer die zweite Stelle des Tripels
     * @param value  = Wert fuer die dritte Stelle des Tripels (Fuer die Matrizen einn Wert)
     */
    public Tripel(int i, int j, double value){
        this.first = i;
        this.second = j;
        this.third = value;
    }
    /**
     * Getter- Methode zum Zurueckgeben des ersten Tripel Elements.
     * @return = Gibt das erste Tripel Element, als Integer zurueck.
     */
    public int _1(){
        return this.first;
    }
/**
     * Getter- Methode zum Zurueckgeben des zweiten Tripel Elements.
     * @return = Gibt das zweite Tripel Element, als Integer zurueck.
     */
    public int _2(){
        return this.second;
    }
/**
     * Getter- Methode zum Zurueckgeben des dritten Tripel Elements.
     * @return = Gibt das dritten Tripel Element, als Integer zurueck.
     */
    public double _3(){
        return this.third;
    }


}
