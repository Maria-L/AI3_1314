/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_5_2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class RekurrenzgleichungTest {

    public RekurrenzgleichungTest() {
    }
    /**
     * Test of rekurrenz method, of class Rekurrenzgleichung.
     */
    @Test
    public void testTask3() {
        int max= 0;
        Rekurrenzgleichung r1 = new Rekurrenzgleichung();
        for(int n= 0; n> -10; n++){
            long r = r1.rekurrenz(n);
            System.out.println(r + " "+ n);
            if(r >= Integer.MAX_VALUE){
                max = n-1;
                break;
            }
        }
        System.out.println(max);
    }
    @Test
    public void testTask4(){
        Rekurrenzgleichung r1 = new Rekurrenzgleichung();
//        for(int n=0; n < 27; n++){
            r1.rekurrenz(26);
//        }
        System.out.println(r1);
    }

    @Test
    public void testTask5(){
        Rekurrenzgleichung r1 = new Rekurrenzgleichung();
        System.out.println(r1.rekurrenz1(9));
        assertEquals(r1.rekurrenz1(9), 861);
    }
   @Test
    public void testTask6(){
        Rekurrenzgleichung r1 = new Rekurrenzgleichung();
        System.out.println(r1.rekurrenz2(9));
        assertEquals(r1.rekurrenz2(9), 861);
    }
}