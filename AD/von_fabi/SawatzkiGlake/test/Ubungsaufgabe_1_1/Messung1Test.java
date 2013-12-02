/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Ubungsaufgabe_1_1;

import Uebungsaufgabe_1_1.Messung1;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class Messung1Test {

     /**
     * Test der Add Methode der Klasse Messung 1. Da als Eingabe ein Double-Wert erfolgt,
      * muss die Methode sowohl mit ganzzahligen Werten als auch mit Gleitkommazahlen im positiven
      * und negativen umgehen können.
     */
    @Test
    public void testAdd() {
        Messung1 messungen = new Messung1();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        assertEquals(9.2,messungen.normalSum(),0.01);
        assertEquals(1675.74,messungen.sqareSum(),0.01);
        assertEquals(6,messungen.counter());
    }

    /**
     * Test der mittelwert Methode der Klasse Messung 1. Die ordnungsmäßige
     * Funktionalität der Add-Methode ist entscheidend für den Mittelwert. Zählt der
     * Counter ordnungsgemäß und wird die normalSum korrekt gebildet, ist der Mittelwert trivial.
      */
    @Test
    public void testMittelwert() {
        Messung1 messungen = new Messung1();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        assertEquals(1.53,messungen.mittelwert(),0.01);
    }

    /**
     * Test der Varianz Methode der Klasse Messung 1. Wieder ist die Funktionalität
     * der Add-Methode entscheidend. Der Rest ergibt sich aus der Definition der Varianz.
     * Das diese korrekt implementiert wurde zeigen wir anhand eines Beispiels.
     */
    @Test
    public void testVarianz() {
        Messung1 messungen = new Messung1();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        assertEquals(332.32, messungen.varianz(),0.01);
    }

}