/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

/**
 *
 * @author abk640
 */
public class Tupel {

    private int first = 0;
    private double second;


    /**
     * Tripel Konstruktor zur Erzeugung eines Tupels
     * @param x = Wert fuer die erste Stelle des Tupels
     * @param y = Wert fuer die zweite Stelle des Tupels
     */
    public Tupel(int x, double y){
        this.first = x;
        this.second = y;
    }
    /**
     * Getter- Methode zum Zurueckgeben des ersten Tupel Elements.
     * @return = Gibt das erste Tupel Element, als Integer zurueck.
     */
    public int _1(){
        return this.first;
    }
    /**
     * Getter- Methode zum Zurueckgeben des zweiten Tupel Elements.
     * @return = Gibt das zweiten Tupel Element, als Integer zurueck.
     */
    public double _2(){
        return this.second;
    }

}
