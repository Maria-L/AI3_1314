/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_1_1;

/**
 *
 * @author abk640
 */
public class Main {

   /**
    * Testmessungen zur Messungsimplementation
    * @param args
    */
    public static void main(String[] args){
        Messung messungen = new Messung();
        messungen.add(2.3);
        messungen.add(-30);
        messungen.add(7);
        messungen.add(-2.4);
        messungen.add(26);
        messungen.add(6.3);
        System.out.println(messungen.mittelwert());
        System.out.println(messungen.varianz());
    }

}
