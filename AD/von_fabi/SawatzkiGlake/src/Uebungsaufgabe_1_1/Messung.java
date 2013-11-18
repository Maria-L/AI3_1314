/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Daniel Glake, Fabian Sawatzki
 */
public class Messung implements IMessung{

    private List<Double> messungen = new LinkedList<Double>();
    private double summe = 0;

    /**
     * Messungsobjekt für Messschleife
     */
    public Messung(){

    }

    /**
     * Neuen Messwert hinzufügen.
     * Die neue Summe aller Messwerte wird berechnet. Dazu steht eine Instanzvariable summe
     * zur Verfügung.
     * @param neuerWert = Neuer Messwert
     */
    public void add(double neuerWert){
        messungen().add(neuerWert);
        summe = (summe + neuerWert);
    }

    /**
     * Getter Methode zum Zurückgeben der Liste.
     * @return List
     */
    public List messungen(){
        return this.messungen;
    }

    /**
     * Berechnung des aktuellen Mittelwertes, mithilfe der Instanzvariable summe
     * sowie die Länge der Messliste.
     * @return = Rückgabe des Mittelwertes
     */
    public double mittelwert(){
        return (summe/messungen.size());
    }

    /**
     * Berechnung der aktuellen korrigierten Standardabweichung, nach der 
     * zur Verfügung gestellten Definition.
     * @return = korrigieret Standardabweichung
     */
    public double varianz(){
        double sum = 0;
        double mittelwert = this.mittelwert();
        for(double e: messungen){
            sum = sum + Math.pow((e-mittelwert),2);
        }
        return (sum/(messungen().size()-1));
    }

}
