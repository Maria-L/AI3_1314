/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_1;

/**
 *
 * @author Daniel Glake, Fabian Sawatzki
 */
public class Messung1 implements IMessung{

    private double normalSum = 0;
    private double sqareSum = 0;
    private int counter = 0;

    /**
     * Konstruktor zur Erzeugung eines neuen Messungsobjekts.
     */
    public Messung1(){ }

    /**
     * Hinzufügen von neune Messwerten, die mit der Summe, bisher eingegebener
     * Messwerte verrechnet werden und mit der Summe quadrierter Einzelwerte
     * verrechnet wird. Für jeden neuen Messwert wird ein Counter um 1 erhöht, der
     * sich merkt, wie viele Messwerte bisher eingetragen wurden.
     * @param neuerWert = Neuer Double Messwert
     */
    public void add(double neuerWert){
        normalSum = normalSum + neuerWert;
        sqareSum = Math.pow(neuerWert, 2) + sqareSum;
        counter = counter + 1;
    }

    /**
     * Getter Methode für die Summer aller Messwerte.
     * @return Summe aller Messwerte
     */
    public double normalSum(){
        return normalSum;
    }

    /**
     * Getter Methode für die Summe, quadrierter Einzel- Messwerte.
     * @return Summe quadrierter Einzel- Messwerte
     */
    public double sqareSum(){
        return sqareSum;
    }

    /**
     * Getter Methode für die Gesamtanzahl der Messwerte.
     * @return Gesamtanzahl der Messwerte
     */
    public int counter(){
        return counter;
    }

    /**
     * Berechnung und Rückgabe des Mittelwertes aller Messwerte.
     * @return Mittelwert, der eingegebenen Messwerte
     */
    public double mittelwert(){
        return this.normalSum()/this.counter();
    }

    /**
     * Berechnung und Rückgabe der korrigierten Varianz.
     * @return Korrigierte Varianz, der eingegebenen Messwerte
     */
    public double varianz(){
        double sum = (this.sqareSum() - (Math.pow(this.normalSum(),2)/this.counter()));
        return (sum/(this.counter()-1));
    }

}
