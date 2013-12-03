/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_3_2;

import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class PotenzierenTest {
    /**
     * Dient zum beispielhaften Test der iterativen Potenzmethode
     */
    @Test
    public void testExp() {
        Potenzieren p = new Potenzieren();
        System.out.println(Integer.MAX_VALUE);
            System.out.println(p.exp(2, 30));
         for(int i = 0; i<=999999;i++){
             p.exp(3, 20);
         }
    }

    @Test
    public void testTask_3_2_3(){
        Random random = new Random();
        Potenzieren p = new Potenzieren();
        p.reset();
        int x = random.nextInt(9);
        int k = random.nextInt(100);
        p.exp01(x, k);
        System.out.println("Anzahl der Berechnungsoperationen (exp01): " + p.counter + " für " + x + " und " + k);
        p.reset();
        p.exp(x, k);
        System.out.println("Anzahl der Berechnungsoperationen (exp): " + p.counter + " für " + x + " und " + k);
    }

    /**
     * Dient zum beispielhaften Test der rekursiven Potenzmethode
     */
    @Test
    public void testExp01() {
            Potenzieren p = new Potenzieren();
            System.out.println(p.exp01(2, 3));
            for(int i = 0; i<= 999999; i++){
                p.exp01(3, 20);
            }
    }

}