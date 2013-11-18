/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Ubungsaufgabe_1_1;

import Uebungsaufgabe_1_1.Messung;
import java.util.List;
import java.util.LinkedList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class MessungTest {
    /**
     * Test der Add Methode der Klasse Messung. Da als Eingabe ein Double-Wert erfolgt,
      * muss die Methode sowohl mit ganzzahligen Werten als auch mit Gleitkommazahlen im positiven
      * und negativen umgehen können.
     */
    @Test
    public void testAdd() {
        Messung messungen = new Messung();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        List<Double> testList = new LinkedList<Double>();
        testList.add(2.3);
        testList.add(-30.0);
        testList.add(7.0);
        testList.add(-2.4);
        testList.add(26.0);
        testList.add(6.3);
        assertEquals(testList,messungen.messungen());
    }

    /**
     * Test der mittelwert Methode der Klasse Messung. Die ordnungsmäßige
     * Funktionalität der Add-Methode ist entscheidend für den Mittelwert. Wurde die Summe
     * korrekt gebildet und jeder Wert in die Messungen-Liste eingepflegt, ist der Mittelwert trivial.
     */
    @Test
    public void testMittelwert() {
        Messung messungen = new Messung();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        assertEquals(1.533,messungen.mittelwert(),0.01);
    }

    /**
     * Test der Varianz Methode der Klasse Messung. Wieder ist die Funktionalität
     * der Add-Methode entscheidend. Der Rest ergibt sich aus der Definition der Varianz.
     * Das diese korrekt implementiert wurde zeigen wir anhand eines Beispiels.
     */
    @Test
    public void testVarianz() {
        Messung messungen = new Messung();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        assertEquals(332.32,messungen.varianz(),0.01);
    }

}