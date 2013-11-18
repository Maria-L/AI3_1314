/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_1;


/**
 * Praktikumsgruppe_2
 * Aufgabe_1
 * @author Daniel Glake, Fabian Sawatzki
 */
public interface IMessung {

     /**
     * Neuen Messwert hinzufügen.
     * @param neuerWert = Neuer Messwert
     */
    public void add(double neuerWert);

    /**
     * Berechnung des aktuellen Mittelwertes.
     * @return = Rückgabe des Mittelwertes
     */
    public double mittelwert();

    /**
     * Berechnung der aktuellen korrigierten Standardabweichung.
     * @return = korrigieret Standardabweichung
     */
    public double varianz();

}
